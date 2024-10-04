package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.Category;

import java.util.List;

public interface CategoryService {
    public Category saveCategory(Category category);

    public Boolean existCategory(String name);
    public List<Category> getAllCategory();
}
