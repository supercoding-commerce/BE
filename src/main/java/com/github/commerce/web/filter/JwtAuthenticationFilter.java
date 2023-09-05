package com.github.commerce.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.commerce.config.security.JwtUtil;
import com.github.commerce.entity.UserRoleEnum;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.dto.user.LoginRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;


    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/v1/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto loginRequestDto=new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),loginRequestDto.getPassword(),null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UserException(UserErrorCode.LOGIN_FAIL);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email=((UserDetailsImpl)authResult.getPrincipal()).getUsername();
        UserRoleEnum role=((UserDetailsImpl)authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(email, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //throw new UserException(UserErrorCode.AUTHENTICATION_FAIL);
        response.setStatus(401);
    }
}
