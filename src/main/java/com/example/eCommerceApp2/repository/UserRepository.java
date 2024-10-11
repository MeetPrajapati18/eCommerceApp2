package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    public UserEntity findByEmail(String email);

    List<UserEntity> findByRole(String role);
}
