package com.github.commerce.web.controller.user;

import com.github.commerce.config.security.JwtUtil;
import com.github.commerce.entity.User;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.user.UserService;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.dto.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "회원관련 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @ApiOperation("판매자 회원가입")
    @PostMapping(value = "/register-seller")
    public ResponseEntity<String> registerSeller(@RequestPart RegisterSellerDto registerSellerDto, @RequestPart(required = false) MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.registerSeller(registerSellerDto, multipartFile));
    }

    @ApiOperation("구매자 회원가입")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserInfoDto registerUserInfoDto) {
        return ResponseEntity.ok(userService.registerUser(registerUserInfoDto));
    }

    @ApiOperation("로그인")
    @PostMapping(value = "/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        TokenDto tokenDto = userService.login(loginRequestDto);
        httpServletResponse.setHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        httpServletResponse.setHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());

        return ResponseEntity.ok(tokenDto);
    }

    @ApiOperation("로그인한 회원 정보 가져오기")
    @GetMapping(value = "/getInfo")
    @PreAuthorize("isAuthenticated()")//메소드 실행 전 로그인되어야함
    public ResponseEntity<MyInfoResponseDto> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        if (user.getIsDelete() == true)
            throw new UserException(UserErrorCode.UER_NOT_FOUND);
        return ResponseEntity.ok(userService.getMyInfo(user.getId(), user.getRole().name()));

    }

    @ApiOperation("이메일 중복 확인")
    @GetMapping(value = "/checkEmail")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.checkEmail(email));
    }

    @ApiOperation("닉네임 중복 확인")
    @GetMapping(value = "/checkNickname")
    public ResponseEntity<String> checkNickName(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.checkNickName(nickname));
    }

    @ApiOperation("쇼핑몰 이름 중복 확인")
    @GetMapping(value = "/checkShopName")
    public ResponseEntity<String> checkShopName(@RequestParam String shopName) {
        return ResponseEntity.ok(userService.checkShopName(shopName));
    }

    @ApiOperation("비밀번호 변경")
    @PatchMapping(value = "/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordReq updatePasswordReq,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.updatePassword(updatePasswordReq,userDetails));
    }

    @ApiOperation("판매자 회원 수정 정보 가져오기")
    @GetMapping(value = "/getSellerInfo")
    public ResponseEntity<SellerInfo> getSellerInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getSellerInfo(userDetails.getUser().getId()));
    }

    @ApiOperation("판매자 회원 정보 수정")
    @PatchMapping(value = "/updateSeller")
    public ResponseEntity<String> updateSeller(@RequestPart SellerInfo sellerInfo, @RequestPart(required = false) MultipartFile shopImgFile,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.updateSeller(sellerInfo, shopImgFile,userDetails.getUser().getId()));
    }

    @ApiOperation("구매자 회원 수정 정보 가져오기")
    @GetMapping(value = "/getUserInfo")
    public ResponseEntity<UserInfo> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getUserInfo(userDetails.getUser().getId()));
    }

    @ApiOperation("구매자 회원 정보 수정")
    @PatchMapping(value = "/update")
    public ResponseEntity<String> updateUserInfo(@RequestBody UserInfo userInfo,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.updateUserInfo(userInfo,userDetails.getUser().getId()));
    }

}