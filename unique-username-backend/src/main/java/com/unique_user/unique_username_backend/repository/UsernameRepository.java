package com.unique_user.unique_username_backend.repository;

import com.unique_user.unique_username_backend.model.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UsernameRepository extends JpaRepository<Username, String> {
    boolean existsByUsername(String username);
}
