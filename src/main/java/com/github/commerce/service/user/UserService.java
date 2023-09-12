package com.github.commerce.service.user;

import com.github.commerce.config.security.JwtUtil;
import com.github.commerce.entity.*;
import com.github.commerce.repository.user.RefreshTokenRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.repository.user.UserInfoRepository;
import com.github.commerce.repository.user.UserRepository;
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

    @Transactional
    public boolean registerSeller(ReigsterSellerDto reigsterSellerDto) {

        //이메일 중복확인
        if (userRepository.existsByEmail(reigsterSellerDto.getEmail())) {
            throw new UserException(UserErrorCode.USER_EMAIL_ALREADY_EXIST);
        }

        //쇼핑몰 이름 중복확인
        if(sellerRepository.existsByShopName(reigsterSellerDto.getShopName())) {
            throw new UserException(UserErrorCode.SHOP_NAME_ALREADY_EXIST);
        }

        User user = User.builder()
                .email(reigsterSellerDto.getEmail())
                .password(passwordEncoder.encode(reigsterSellerDto.getPassword()))
                .userName(reigsterSellerDto.getUserName())
                .telephone(reigsterSellerDto.getTelephone())
                .role(UserRoleEnum.SELLER)
                .isDelete(false)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        sellerRepository.save(
                Seller.builder()
                        .users(user)
                        .shopName(reigsterSellerDto.getShopName())
                        .address(reigsterSellerDto.getAddress())
                        .build());

        return true;
    }

    @Transactional
    public boolean registerUser(RegisterUserInfoDto registerUserInfoDto) {

        //이메일 중복확인
        if (userRepository.existsByEmail(registerUserInfoDto.getEmail())) {
            throw new UserException(UserErrorCode.USER_EMAIL_ALREADY_EXIST);
        }

        //닉네임 중복확인
        if(userInfoRepository.existsByNickname(registerUserInfoDto.getNickname())) {
            throw new UserException(UserErrorCode.USER_NICKNAME_ALREADY_EXIST);
        }

        User user = User.builder()
                .email(registerUserInfoDto.getEmail())
                .password(passwordEncoder.encode(registerUserInfoDto.getPassword()))
                .userName(registerUserInfoDto.getUserName())
                .telephone(registerUserInfoDto.getTelephone())
                .role(UserRoleEnum.USER)
                .isDelete(false)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        userInfoRepository.save(
                UsersInfo.builder()
                        .users(user)
                        .grade(Grade.GREEN)
                        .gender(registerUserInfoDto.getGender())
                        .address(registerUserInfoDto.getAddress())
                        .age(registerUserInfoDto.getAge())
                        .nickname(registerUserInfoDto.getNickname())
                        .build()
        );

        return true;
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {

        String email=loginRequestDto.getEmail();
        String password=loginRequestDto.getPassword();

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByEmail(email);
            if(user==null||user.getIsDelete()==true) {
                throw new UserException(UserErrorCode.UER_NOT_FOUND);
            }

            // 아이디 정보로 Token생성
            TokenDto tokenDto = jwtUtil.createAllToken(user.getEmail(), user.getRole());

            // Refresh토큰 있는지 확인
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(loginRequestDto.getEmail());

            // 있다면 새토큰 발급후 업데이트
            // 없다면 새로 만들고 디비 저장
            if(refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            }else {
                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequestDto.getEmail());
                refreshTokenRepository.save(newToken);
            }

            return tokenDto;

        }catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(UserErrorCode.LOGIN_FAIL);
        }
    }

    public UserInfoResponseDto getUserInfo(Long userId, String role) {

        if(role.equals("SELLER"))
        {
            Seller seller = sellerRepository.findByUsersId(userId).orElseThrow(()->new UserException(UserErrorCode.UER_NOT_FOUND));
            return UserInfoResponseDto.builder()
                    .role(seller.getUsers().getRole().name())
                    //.address(seller.getAddress())
                    .build();
        } else {
            UsersInfo usersInfo = userInfoRepository.findByUsersId(userId).orElseThrow(()->new UserException(UserErrorCode.UER_NOT_FOUND));
            return UserInfoResponseDto.builder()
                    .role(usersInfo.getUsers().getRole().name())
                    .grade(usersInfo.getGrade().name())
                    .nickname(usersInfo.getNickname())
                    .address(usersInfo.getAddress())
                    .build();
        }
    }

    public String checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.USER_EMAIL_ALREADY_EXIST);
        } else return "사용 가능한 이메일입니다.";
    }

    public String checkNickName(String nickName) {
        if(userInfoRepository.existsByNickname(nickName)) {
            throw new UserException(UserErrorCode.USER_NICKNAME_ALREADY_EXIST);
        } else return "사용 가능한 닉네임입니다.";
    }

    public String checkShopName(String shopName) {
        if(sellerRepository.existsByShopName(shopName)) {
            throw new UserException(UserErrorCode.SHOP_NAME_ALREADY_EXIST);
        } else return "사용 가능한 쇼핑몰 이름입니다.";
    }
}
