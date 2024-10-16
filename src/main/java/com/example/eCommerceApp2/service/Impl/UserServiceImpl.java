package com.example.eCommerceApp2.service.Impl;

import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.repository.UserRepository;
import com.example.eCommerceApp2.service.UserService;
import com.example.eCommerceApp2.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.engine.TemplateManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> getUsers(String role){
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(String id, Boolean status) {
        Optional<UserEntity> findByUser = userRepository.findById(id);
        if(findByUser.isPresent()){
            UserEntity userDetails = findByUser.get();
            userDetails.setEnable(status);
            userRepository.save(userDetails);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(UserEntity user) {
        int attempt = user.getFailedAttempt() != null ? user.getFailedAttempt() + 1 : 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(UserEntity user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public Boolean unlockAccountTimeExpired(UserEntity user) {
        long lockTime = user.getLockTime().getTime();
        long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unlockTime < currentTime){
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempt(String userId) {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            // Set the reset token
            user.setResetToken(resetToken);
            // Save the updated user entity to the repository
            userRepository.save(user);
        } else {
            // Handle the case where the user is not found
            System.out.println("User not found with email: " + email);
        }
    }

    @Override
    public UserEntity getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }

}
