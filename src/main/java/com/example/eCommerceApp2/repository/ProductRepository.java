package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByIsActiveTrue();

    List<Product> findByCategory(String category);
}
