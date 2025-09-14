package com.proksi.assessment.component;

import com.proksi.assessment.entity.ApiUser;
import com.proksi.assessment.enums.Role;
import com.proksi.assessment.repository.ApiUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final ApiUserRepository apiUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(ApiUserRepository apiUserRepository, PasswordEncoder passwordEncoder) {
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (apiUserRepository.findByEmail("admin@admin.com").isEmpty()) {
            ApiUser admin = new ApiUser();
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            apiUserRepository.save(admin);

            System.out.println("Admin is created.");
        } else {
            System.out.println("Admin already exists.");
        }
    }
}
