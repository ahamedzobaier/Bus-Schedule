package com.uu.bus.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uu.bus.model.User;
import com.uu.bus.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUUIdEmail(String email) {
        return email != null && email.toLowerCase().endsWith("@uttarauniversity.edu.bd");
    }

    public User registerUser(User user) throws Exception {
        if (!isUUIdEmail(user.getEmail())) {
            throw new Exception("Only @uttarauniversity.edu.bd emails are allowed!");
        }
        return userRepository.save(user);
    }

    public User login(String emailOrId, String password) {
        // Check by Email
        Optional<User> userByEmail = userRepository.findByEmail(emailOrId);
        if (userByEmail.isPresent() && userByEmail.get().getPassword().equals(password)) {
            return userByEmail.get();
        }
        // Check by University ID
        Optional<User> userById = userRepository.findByUniversityId(emailOrId);
        if (userById.isPresent() && userById.get().getPassword().equals(password)) {
            return userById.get();
        }
        return null;
    }
}