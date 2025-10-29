package com.bank.rest.domainRoles;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

public enum UserRole {ADMIN, USER;
        public List<SimpleGrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
        }
    }
