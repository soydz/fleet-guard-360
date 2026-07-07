package com.fleetguard360.presentation.controller;

import com.fleetguard360.presentation.dto.AuthReqDTO;
import com.fleetguard360.presentation.dto.AuthResDTO;
import com.fleetguard360.service.interfaces.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResDTO> login(@RequestBody AuthReqDTO authReq) {
        return ResponseEntity.ok(authService.login(
                authReq.email(),
                authReq.password()
        ));
    }
}
