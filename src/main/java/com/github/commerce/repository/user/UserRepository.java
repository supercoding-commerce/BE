package com.github.commerce.repository.user;

import com.github.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String userEmail);
    User findUserById(Long id);

<<<<<<< Updated upstream
    boolean existsByEmail(String email);

    String findRoleById(Long profileId);
=======
    boolean existsByEmailAndIsDeleteIsFalse(String email);
>>>>>>> Stashed changes
}
