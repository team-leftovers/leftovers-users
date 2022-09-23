package com.leftovers.user.repository;

import com.leftovers.user.model.SiteAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteAdminRepository extends JpaRepository<SiteAdmin, Long> {
    Optional<SiteAdmin> findByEmail(String email);
}
