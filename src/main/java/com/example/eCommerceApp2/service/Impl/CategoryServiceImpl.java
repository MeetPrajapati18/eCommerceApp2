package com.example.eCommerceApp2.service.Impl; // Adjusted package name to 'impl'

import com.example.eCommerceApp2.model.Category;
import com.example.eCommerceApp2.repository.CategoryRepository;
import com.example.eCommerceApp2.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service // Indicates that this class is a Spring service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired // Constructor-based dependency injection
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean existsCategory(String name) {
        return categoryRepository.existsByName(name); // Update here
    }

    @Override
    public Boolean deleteCategory(String id) {  // Keep the ID as int
        // Use Integer for MongoDB ID
        Category category = categoryRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(category)) {
            categoryRepository.delete(category);
            return true;
        }
        return false;  // Return false if the category was not found
    }

    @Override
    public Category getCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> allCategories = categoryRepository.findByIsActiveTrue();
        return allCategories;
    }

    @Override
    public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return categoryRepository.findAll(pageable);
    }
}
