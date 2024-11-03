package com.example.eCommerceApp2.service.Impl;

import com.example.eCommerceApp2.model.Product;
import com.example.eCommerceApp2.repository.ProductRepository;
import com.example.eCommerceApp2.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(String Id) {
        Product product = productRepository.findById(Id).orElse(null);
        if(!ObjectUtils.isEmpty(product)){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(String Id) {
        Product product = productRepository.findById(Id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image, HttpSession session) {
        Product dbProduct = getProductById(product.getId());

        String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();

        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setImage(imageName);
        dbProduct.setActive(product.getActive());
        dbProduct.setDiscount(product.getDiscount());

        // 5=100*(5/100); 100-5=95
        Double discount = product.getPrice() * (product.getDiscount() / 100.0);
        Double discountPrice = product.getPrice() - discount;
        dbProduct.setDiscountPrice(discountPrice);

        Product updateProduct = productRepository.save(dbProduct);

        if (!ObjectUtils.isEmpty(updateProduct)) {

            if (!image.isEmpty()) {

                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/product_img";
                File saveFile = new File(uploadDir);
                Path filePath = Paths.get(saveFile.getAbsolutePath() + File.separator + image.getOriginalFilename());
                try {
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return product;
        }
        return null;
    }

    @Override
    public List<Product> getAllActiveProduct(String category) {
        List<Product> products = null;
        if(ObjectUtils.isEmpty(category)){
            products = productRepository.findByIsActiveTrue();
        } else {
            products = productRepository.findByCategory(category);
        }

        return products;
    }

    @Override
    public List<Product> searchProduct(String ch) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }

    @Override
    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> pageProduct = null;

        if(ObjectUtils.isEmpty(category)){
            pageProduct = productRepository.findByIsActiveTrue(pageable);
        } else {
            pageProduct = productRepository.findByCategory(pageable, category);
        }

        return pageProduct;
    }
}
