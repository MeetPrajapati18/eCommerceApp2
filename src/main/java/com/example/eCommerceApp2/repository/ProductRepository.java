package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
