package com.github.commerce.web.controller.user;

import com.github.commerce.service.user.UserService;
import com.github.commerce.web.dto.user.RegisterUserInfoDto;
import com.github.commerce.web.dto.user.ReigsterSellerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register-seller")
    public String registerSeller(@RequestBody ReigsterSellerDto reigsterSellerDto) {
        boolean isSuccess=userService.registerSeller(reigsterSellerDto);
        return isSuccess ? "회원가입 성공!" : "회원가입 실패!";
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterUserInfoDto registerUserInfoDto) {
        boolean isSuccess=userService.registerUser(registerUserInfoDto);
        return isSuccess ? "회원가입 성공!" : "회원가입 실패!";
    }
}