package com.github.commerce.service.user;

import com.github.commerce.entity.User;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);

        //존재하지 않는 유저
        if(user ==null||user.getIsDelete()==true) {
            throw new UserException(UserErrorCode.UER_NOT_FOUND);
        }

        return new UserDetailsImpl(user);
    }
}
