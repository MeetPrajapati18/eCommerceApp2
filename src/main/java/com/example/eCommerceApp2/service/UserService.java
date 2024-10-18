package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    UserEntity getUserByEmail(String email);

    List<UserEntity> getUsers(String role);

    Boolean updateAccountStatus(String id, Boolean status);

    void increaseFailedAttempt(UserEntity user);

    void userAccountLock(UserEntity user);

    Boolean unlockAccountTimeExpired(UserEntity user);

    void resetAttempt(String userId);

    void updateUserResetToken(String email, String resetToken);

    UserEntity getUserByToken(String token);

    UserEntity updateUser(UserEntity user);
}
