package com.github.commerce.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.commerce.config.security.JwtUtil;
import com.github.commerce.entity.*;
import com.github.commerce.repository.user.RefreshTokenRepository;
import com.github.commerce.repository.user.UserInfoRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.coupon.UserCouponService;
import com.github.commerce.web.dto.user.KakaoUserInfoDto;
import com.github.commerce.web.dto.user.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserCouponService userCouponService;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public TokenDto kakaoLogin(String code) throws JsonProcessingException {

        String accessToken = getToken(code);
        //토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        User user = findOrCreateUser(kakaoUserInfo);
        TokenDto tokenDto=oAuthLogin(user);

        return tokenDto;
    }

    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "2b71aceca0732b8f9db4295e0f78276f");
        body.add("redirect_uri", "http://localhost:5173/v1/api/user/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        //log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    @Transactional
    public User findOrCreateUser(KakaoUserInfoDto kakaoUserInfo) {
        User user = userRepository.findUserByEmail(kakaoUserInfo.getEmail());
        if(user==null||user.getIsDelete()==true) {
            String password= UUID.randomUUID().toString();
            User savedUser = userRepository.save(
                    User.builder()
                            .email(kakaoUserInfo.getEmail())
                            .password(passwordEncoder.encode(password))
                            .userName(kakaoUserInfo.getNickname())
                            .role(UserRoleEnum.USER)
                            .isDelete(false)
                            .createdAt(LocalDateTime.now())
                            .build()
            );
            userInfoRepository.save(
                    UsersInfo.builder()
                            .users(savedUser)
                            .grade(Grade.GREEN)
                            .nickname(Long.toString(kakaoUserInfo.getId()))
                            .gender("")
                            .address("")
                            .age("")
                            .build()
            );

            //회원가입시 자동 신규회원 쿠폰발급
            userCouponService.issueUserCoupon(savedUser.getId(), 1l);
            return user;

        }
        return user;
    }

    private TokenDto oAuthLogin(User user) {
        TokenDto tokenDto=jwtUtil.createAllToken(user.getEmail(),user.getRole());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(user.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user.getEmail());
            refreshTokenRepository.save(newToken);
        }
        return tokenDto;
    }

}

