package com.github.commerce.service.user;

import com.github.commerce.config.security.JwtUtil;
import com.github.commerce.entity.*;
import com.github.commerce.repository.user.*;
import com.github.commerce.service.coupon.UserCouponService;
import com.github.commerce.service.product.AwsS3Service;
import com.github.commerce.service.product.ProductImageUploadService;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.dto.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProductImageUploadService productImageUploadService;
    private final AwsS3Service awsS3Service;
    private final UserCouponService userCouponService;

    @Transactional
    public String registerSeller(RegisterSellerDto registerSellerDto, MultipartFile shopImgFile) {

        //이메일 중복확인
        if (userRepository.existsByEmailAndIsDeleteIsFalse(registerSellerDto.getEmail())) {
            throw new UserException(UserErrorCode.USER_EMAIL_ALREADY_EXIST);
        }

        //쇼핑몰 이름 중복확인
        if (sellerRepository.existsByShopNameAndUsersIsDeleteFalse(registerSellerDto.getShopName())) {
            throw new UserException(UserErrorCode.SHOP_NAME_ALREADY_EXIST);
        }

        //비밀번호 유효성 검사
        if (!registerSellerDto.getPassword().matches("^[a-zA-Z0-9]{8,20}$")) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_UPDATE_PASSWORD);
        }

        User user = userRepository.save(
                User.builder()
                        .email(registerSellerDto.getEmail())
                        .password(passwordEncoder.encode(registerSellerDto.getPassword()))
                        .userName(registerSellerDto.getUserName())
                        .telephone(registerSellerDto.getTelephone())
                        .role(UserRoleEnum.SELLER)
                        .isDelete(false)
                        .createdAt(LocalDateTime.now())
                        .build());

        Seller seller =
                Seller.builder()
                        .users(user)
                        .shopName(registerSellerDto.getShopName())
                        .address(registerSellerDto.getAddress())
                        .addressDetail(registerSellerDto.getAddressDetail())
                        .build();

        if (shopImgFile != null) {//이미지 파일 있으면 저장
            String imageUrl = productImageUploadService.uploadShopImage(shopImgFile);
            seller.setShopImageUrl(imageUrl);
        }
        sellerRepository.save(seller);


        return "회원가입 완료 되었습니다.";
    }

    @Transactional
    public String registerUser(RegisterUserInfoDto registerUserInfoDto) {

        //이메일 중복확인
        if (userRepository.existsByEmailAndIsDeleteIsFalse(registerUserInfoDto.getEmail())) {
            throw new UserException(UserErrorCode.USER_EMAIL_ALREADY_EXIST);
        }

        //닉네임 중복확인
        if (userInfoRepository.existsByNicknameAndUsersIsDeleteFalse(registerUserInfoDto.getNickname())) {
            throw new UserException(UserErrorCode.USER_NICKNAME_ALREADY_EXIST);
        }

        //비밀번호 유효성 검사
        if (!registerUserInfoDto.getPassword().matches("^[a-zA-Z0-9]{8,20}$")) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_UPDATE_PASSWORD);
        }

        User user = userRepository.save(
                User.builder()
                        .email(registerUserInfoDto.getEmail())
                        .password(passwordEncoder.encode(registerUserInfoDto.getPassword()))
                        .userName(registerUserInfoDto.getUserName())
                        .telephone(registerUserInfoDto.getTelephone())
                        .role(UserRoleEnum.USER)
                        .isDelete(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        userInfoRepository.save(
                UsersInfo.builder()
                        .users(user)
                        .grade(Grade.GREEN)
                        .gender(registerUserInfoDto.getGender())
                        .address(registerUserInfoDto.getAddress())
                        .addressDetail(registerUserInfoDto.getAddressDetail())
                        .age(registerUserInfoDto.getAge())
                        .nickname(registerUserInfoDto.getNickname())
                        .build()
        );

        //회원가입시 자동 신규회원 쿠폰발급
        userCouponService.issueUserCoupon(user.getId(), 1l);

        return "회원가입 완료되었습니다.";
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByEmail(email);
            if (user == null || user.getIsDelete() == true) {
                throw new UserException(UserErrorCode.UER_NOT_FOUND);
            }

            // 아이디 정보로 Token생성
            TokenDto tokenDto = jwtUtil.createAllToken(user.getEmail(), user.getRole());

            // Refresh토큰 있는지 확인
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(loginRequestDto.getEmail());

            // 있다면 새토큰 발급후 업데이트
            // 없다면 새로 만들고 디비 저장
            if (refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            } else {
                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequestDto.getEmail());
                refreshTokenRepository.save(newToken);
            }

            return tokenDto;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(UserErrorCode.LOGIN_FAIL);
        }
    }

    public MyInfoResponseDto getMyInfo(Long userId, String role) {

        if (role.equals("SELLER")) {
            Seller seller = sellerRepository.findByUsersId(userId).orElseThrow(() -> new UserException(UserErrorCode.UER_NOT_FOUND));
            return MyInfoResponseDto.builder()
                    .role(seller.getUsers().getRole().name())
                    //.address(seller.getAddress())
                    .build();
        } else {
            UsersInfo usersInfo = userInfoRepository.findByUsersId(userId).orElseThrow(() -> new UserException(UserErrorCode.UER_NOT_FOUND));
            return MyInfoResponseDto.builder()
                    .role(usersInfo.getUsers().getRole().name())
                    .grade(usersInfo.getGrade().name())
                    .nickname(usersInfo.getNickname())
                    .address(usersInfo.getAddress())
                    .payMoney(usersInfo.getUsers().getPayMoneyByUserId().getPayMoneyBalance())
                    .build();
        }
    }

    public String checkEmail(String email) {
        if (userRepository.existsByEmailAndIsDeleteIsFalse(email)) {
            throw new UserException(UserErrorCode.USER_EMAIL_ALREADY_EXIST);
        } else return "사용 가능한 이메일입니다.";
    }

    public String checkNickName(String nickname) {
        if (userInfoRepository.existsByNicknameAndUsersIsDeleteFalse(nickname)) {
            throw new UserException(UserErrorCode.USER_NICKNAME_ALREADY_EXIST);
        } else return "사용 가능한 닉네임입니다.";
    }

    public String checkShopName(String shopName) {
        if (sellerRepository.existsByShopNameAndUsersIsDeleteFalse(shopName)) {
            throw new UserException(UserErrorCode.SHOP_NAME_ALREADY_EXIST);
        } else return "사용 가능한 쇼핑몰 이름입니다.";
    }

    @Transactional
    public String updatePassword(UpdatePasswordReq updatePasswordReq, UserDetailsImpl userDetails) {
        User user = userRepository.findUserById(userDetails.getUser().getId());

        //기존 비밀 번호와 일치 여부 확인
        if (!passwordEncoder.matches(updatePasswordReq.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.NOT_EQUL_PASSWORD);
        }
        //새로운 비밀번호 유효성 검사
        if (!updatePasswordReq.getNewPassword().matches("^[a-zA-Z0-9]{8,20}$")) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_UPDATE_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordReq.getNewPassword()));
        return "비밀번호 변경 완료되었습니다.";


    }

    public SellerInfo getSellerInfo(Long userId) {
        Optional<Seller> seller = sellerRepository.findByUsersId(userId);

        if (seller.isEmpty() || seller.get().getUsers().getIsDelete() == true) {
            throw new UserException(UserErrorCode.UER_NOT_FOUND);
        }

        return SellerInfo.builder()
                .userName(seller.get().getUsers().getUserName())
                .telephone(seller.get().getUsers().getTelephone())
                .shopName(seller.get().getShopName())
                .address(seller.get().getAddress())
                .addressDetail(seller.get().getAddressDetail())
                .shopImageUrl(seller.get().getShopImageUrl())
                .build();

    }

    @Transactional
    public String updateSeller(SellerInfo sellerInfo, MultipartFile shopImgFile, Long userId) {

        Optional<Seller> seller = sellerRepository.findByUsersId(userId);

        if (seller.isEmpty() || seller.get().getUsers().getIsDelete() == true) {
            throw new UserException(UserErrorCode.UER_NOT_FOUND);
        }

        //쇼핑몰 이름 변경있으면 중복확인
        if (!sellerInfo.getShopName().equals(seller.get().getShopName())) {
            if (sellerRepository.existsByShopNameAndUsersIsDeleteFalse(sellerInfo.getShopName())) {
                throw new UserException(UserErrorCode.SHOP_NAME_ALREADY_EXIST);
            }
        }

        seller.get().getUsers().setUserName(sellerInfo.getUserName());
        seller.get().getUsers().setTelephone(sellerInfo.getTelephone());
        seller.get().setShopName(sellerInfo.getShopName());
        seller.get().setAddress(sellerInfo.getAddress());
        seller.get().setAddressDetail(sellerInfo.getAddressDetail());

        if (shopImgFile != null) {//이미지 파일 있으면 기존 파일 삭제 후 새로운 파일 저장
            awsS3Service.removeFile(sellerInfo.getShopImageUrl());
            String imageUrl = productImageUploadService.uploadShopImage(shopImgFile);
            seller.get().setShopImageUrl(imageUrl);
        }

        return "회원정보 수정 되었습니다!";
    }


    public UserInfo getUserInfo(Long userId) {
        Optional<UsersInfo> userInfo = userInfoRepository.findByUsersId(userId);

        if (userInfo.isEmpty() || userInfo.get().getUsers().getIsDelete() == true) {
            throw new UserException(UserErrorCode.UER_NOT_FOUND);
        }

        return UserInfo.builder()
                .userName(userInfo.get().getUsers().getUserName())
                .telephone(userInfo.get().getUsers().getTelephone())
                .address(userInfo.get().getAddress())
                .addressDetail(userInfo.get().getAddressDetail())
                .nickname(userInfo.get().getNickname())
                .build();
    }

    @Transactional
    public String updateUserInfo(UserInfo userInfoDto, Long userId) {
        Optional<UsersInfo> userInfo = userInfoRepository.findByUsersId(userId);

        if (userInfo.isEmpty() || userInfo.get().getUsers().getIsDelete() == true) {
            throw new UserException(UserErrorCode.UER_NOT_FOUND);
        }

        //닉네임 변경있으면 중복확인
        if (!userInfoDto.getNickname().equals(userInfo.get().getNickname())) {
            if (userInfoRepository.existsByNicknameAndUsersIsDeleteFalse(userInfoDto.getNickname())) {
                throw new UserException(UserErrorCode.USER_NICKNAME_ALREADY_EXIST);
            }
        }

        userInfo.get().getUsers().setUserName(userInfoDto.getUserName());
        userInfo.get().getUsers().setTelephone(userInfoDto.getTelephone());
        userInfo.get().setAddress(userInfoDto.getAddress());
        userInfo.get().setAddressDetail(userInfoDto.getAddressDetail());
        userInfo.get().setNickname(userInfoDto.getNickname());

        return "회원정보 수정 되었습니다!";
    }
}
