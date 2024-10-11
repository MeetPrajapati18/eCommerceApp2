package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.Category;

import java.util.List;

public interface CategoryService {
    Category saveCategory(Category category); // No need for public as it's implicit in an interface

    List<Category> getAllCategories(); // Changed to plural for consistency

    Boolean existsCategory(String name); // Keep this method only

    Boolean deleteCategory(String id);

    Category getCategoryById(String id);

    List<Category> getAllActiveCategory();
}