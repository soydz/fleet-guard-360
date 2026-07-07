package com.fleetguard360.service.interfaces;

import com.fleetguard360.presentation.dto.AuthResDTO;

public interface AuthService {
    AuthResDTO login(String email, String password);
}
