package com.github.commerce.service.user;

import com.github.commerce.entity.*;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.repository.user.UserInfoRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.dto.user.RegisterUserInfoDto;
import com.github.commerce.web.dto.user.ReigsterSellerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

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
                //.password(reigsterSellerDto.getPassword())
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
                //.password(registerUserInfoDto.getPassword())
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
                        .grade(Grade.Green.name())
                        .gender(registerUserInfoDto.getGender())
                        .address(registerUserInfoDto.getAddress())
                        .age(registerUserInfoDto.getAge())
                        .nickname(registerUserInfoDto.getNickname())
                        .build()
        );

        return true;
    }

}
