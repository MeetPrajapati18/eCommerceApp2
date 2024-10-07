package com.example.eCommerceApp2.service.Impl;

import com.example.eCommerceApp2.model.Product;
import com.example.eCommerceApp2.repository.ProductRepository;
import com.example.eCommerceApp2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
