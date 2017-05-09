package com.volontyr.repository;

import com.volontyr.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by volontyr on 05.05.17.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
