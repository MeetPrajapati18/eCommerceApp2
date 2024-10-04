package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Boolean existByName(String name);
}
