package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    public UserEntity getUserByEmail(String email);

    public List<UserEntity> getUsers(String role);

    Boolean updateAccountStatus(String id, Boolean status);
}
