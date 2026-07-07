package com.fleetguard360.service.implementation;

import com.fleetguard360.persistence.entity.User;
import com.fleetguard360.persistence.repository.UserRepository;
import com.fleetguard360.service.exception.ResourceNotFoundException;
import com.fleetguard360.service.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con id: " + id + " no encontrado"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email: " + email + " no encontrado"));
    }
}
