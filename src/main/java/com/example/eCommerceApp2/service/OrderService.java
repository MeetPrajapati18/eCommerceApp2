package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.OrderRequest;
import com.example.eCommerceApp2.model.ProductOrder;

import java.util.List;

public interface OrderService {

    void saveOrder(String userId, OrderRequest orderRequest);

    List<ProductOrder> getOrderByUser(String userId);

    Boolean updateOrderStatus(String id, String status);
}
