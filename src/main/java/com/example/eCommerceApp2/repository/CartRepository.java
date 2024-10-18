package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    Cart findByProductIdAndUserId (String productId, String userId);

    Integer countByUserId(String userid);

    List<Cart> findByUserId(String userId);
}
