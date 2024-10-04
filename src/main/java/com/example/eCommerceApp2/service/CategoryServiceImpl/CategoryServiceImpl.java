package com.example.eCommerceApp2.service.CategoryServiceImpl;

import com.example.eCommerceApp2.model.Category;
import com.example.eCommerceApp2.repository.CategoryRepository;
import com.example.eCommerceApp2.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}
