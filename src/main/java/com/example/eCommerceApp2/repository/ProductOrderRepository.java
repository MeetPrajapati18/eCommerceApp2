package com.example.eCommerceApp2.repository;

import com.example.eCommerceApp2.model.ProductOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductOrderRepository extends MongoRepository <ProductOrder, String> {

    List<ProductOrder> findByUserId(String userId);

    ProductOrder findByOrderId(String orderId);
}
