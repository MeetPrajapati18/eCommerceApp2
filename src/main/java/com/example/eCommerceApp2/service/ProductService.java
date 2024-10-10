package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(String Id);

    public Product getProductById(String Id);

    public Product updateProduct(Product product, MultipartFile file, HttpSession session);

    public List<Product> getAllActiveProduct(String category);
}
