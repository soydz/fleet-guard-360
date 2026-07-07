package com.fleetguard360.service.implementation;

import com.fleetguard360.persistence.entity.User;
import com.fleetguard360.persistence.repository.UserRepository;
import com.fleetguard360.service.exception.ResourceNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Bad credentials"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Roles
        user.getRoles()
                .forEach(role -> authorityList.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))
                ));

        // Permisos
        user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorityList.add(
                        new SimpleGrantedAuthority(permission.getPermission().name())
                ));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(user.isDisabled())
                .accountExpired(user.isAccountExpired()) //
                .credentialsExpired(user.isCredentialExpired())
                .accountLocked(user.isAccountLocked())
                .authorities(authorityList)
                .build();
    }
}
