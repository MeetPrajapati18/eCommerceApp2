package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Boolean existsByName(String name);
}
