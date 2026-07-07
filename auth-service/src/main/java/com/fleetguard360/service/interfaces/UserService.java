package com.fleetguard360.service.interfaces;

import com.fleetguard360.persistence.entity.User;

public interface UserService {
    User getById(Long id);

    User getByEmail(String email);
}
