package com.github.commerce.repository.user;

import com.github.commerce.entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UsersInfo, Long> {

    Optional<UsersInfo> findByUsersId(Long userId);
    boolean existsByNicknameAndUsersIsDeleteFalse(String nickname);
}
