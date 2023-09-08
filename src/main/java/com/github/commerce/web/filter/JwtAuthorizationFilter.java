package com.github.commerce.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.commerce.config.security.JwtUtil;
import com.github.commerce.entity.UserRoleEnum;
import com.github.commerce.service.user.UserDetailsServiceImpl;
import com.github.commerce.web.advice.exception.ErrorResponse;
import com.github.commerce.web.advice.exception.type.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

       // try {
            if (StringUtils.hasText(accessToken)) {
                if (jwtUtil.tokenValidation(accessToken)) {//access토큰값 유효
                    Claims info = jwtUtil.getUserInfoFromToken(accessToken);
                    setAuthentication(info.getSubject());

                } else if (StringUtils.hasText(refreshToken)) {
                    /// 어세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
                    // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
                    boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
                    // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
                    if (isRefreshToken) {
                        // 리프레시 토큰으로 정보 가져오기
                        Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
                        UserRoleEnum role = UserRoleEnum.valueOf((String) info.get(JwtUtil.AUTHORIZATION_KEY));

                        String newAccessToken = jwtUtil.createToken(info.getSubject(), role, "Access");
                        response.setHeader("ACCESS_TOKEN", newAccessToken);
                        // Security context에 인증 정보 넣기
                        setAuthentication(jwtUtil.getUserInfoFromToken(newAccessToken.substring(7)).getSubject());


                    } else {// 리프레시 토큰이 만료 || 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
                        jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                        return;

                    }

                }

            }

        filterChain.doFilter(request, response);
    }

    //인증 처리
    private void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // Jwt 예외처리
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ErrorResponse( ErrorCode.BAD_REQUEST,msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
