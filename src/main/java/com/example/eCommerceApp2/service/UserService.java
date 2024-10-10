package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.UserEntity;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    public UserEntity getUserByEmail(String email);
}
