package com.example.eCommerceApp2.service.Impl;

import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.repository.UserRepository;
import com.example.eCommerceApp2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity saveUser(UserEntity user){
        user.setRole("ROLE_USER");
        user.setEnable(true);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        UserEntity saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> getUsers(String role){
        return userRepository.findByRole(role);
    }

}
