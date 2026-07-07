package com.fleetguard360.service.implementation;

import com.fleetguard360.presentation.dto.AuthResDTO;
import com.fleetguard360.service.exception.InvalidCredentialsException;
import com.fleetguard360.service.interfaces.AuthService;
import com.fleetguard360.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResDTO login(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            UserDetails user = (UserDetails) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.createToken(authentication);

            log.info("Credenciales Correctas");

            return new AuthResDTO(
                    user.getUsername(),
                    "Loggeado",
                    accessToken,
                    user.isEnabled());

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Bad credentials");
        }
    }
}
