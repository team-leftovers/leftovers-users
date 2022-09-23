package com.leftovers.user.services;

import com.leftovers.user.exception.InvalidAdminEmailException;
import com.leftovers.user.model.SiteAdmin;
import com.leftovers.user.repository.SiteAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteAdminService {
    private final SiteAdminRepository adminRepository;

    public SiteAdmin createSiteAdmin(String email, String password) {
        if (!email.endsWith("@smoothstack.com")) {
            throw new InvalidAdminEmailException(email);
        }

        SiteAdmin admin = SiteAdmin.builder()
                .email(email)
                .password(password)
                .build();

        return adminRepository.save(admin);
    }
}
