package com.bootlabs.digest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Collection;

@RestController
@RequestMapping("/api")
public class AccountController {

    @GetMapping("/account")
    public ResponseEntity<String> getUserInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        var userData = MessageFormat.format("username:{0} - authorities:{1}", authentication.getName() ,authorities);

        return ResponseEntity.ok(userData);
    }

}
