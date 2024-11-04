package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    Boolean deleteProduct(String Id);

    Product getProductById(String Id);

    Product updateProduct(Product product, MultipartFile file, HttpSession session);

    List<Product> getAllActiveProduct(String category);

    List<Product> searchProduct(String ch);

    Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    Page<Product> searchProductPagination(String ch, Integer pageNo, Integer pageSize);

    Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);
}
