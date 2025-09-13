package com.proksi.assessment.repository;

import com.proksi.assessment.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
    boolean existsByEmail(String email);

    Optional<ApiUser> findByEmail(String email);
}
