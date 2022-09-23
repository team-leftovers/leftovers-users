package com.leftovers.user.controller;

import com.leftovers.user.dto.RegisterAdminDto;
import com.leftovers.user.dto.SiteAdminDto;
import com.leftovers.user.model.SiteAdmin;
import com.leftovers.user.repository.SiteAdminRepository;
import com.leftovers.user.services.AccountService;
import com.leftovers.user.services.SiteAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class SiteAdminController {
    private final SiteAdminService adminService;
    private final SiteAdminRepository adminRepository;

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<SiteAdminDto> registerAdmin(@Valid @RequestBody RegisterAdminDto adminDto) {
        var admin = adminService.createSiteAdmin(adminDto.getEmail(), adminDto.getPassword());

        return ResponseEntity
                .created(URI.create("/admin/" + admin.getId()))
                .body(
                        SiteAdminDto.builder()
                                .id(admin.getId())
                                .email(admin.getEmail())
                                .build());
    }

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN')")
    @RequestMapping(value = "/{adminId}", method = RequestMethod.GET)
    public ResponseEntity<SiteAdmin> getAdminById(@PathVariable Long adminId) {
        // TODO(Jordan): Perform search using Service
        return ResponseEntity.of(adminRepository.findById(adminId));
    }

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN')")
    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<SiteAdmin> getAdminByEmail(@PathVariable String email) {
        // TODO(Jordan): Perform search using Service
        return ResponseEntity.of(adminRepository.findByEmail(email));
    }
}
