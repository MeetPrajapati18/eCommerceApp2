package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.OrderRequest;
import com.example.eCommerceApp2.model.ProductOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    void saveOrder(String userId, OrderRequest orderRequest);

    List<ProductOrder> getOrderByUser(String userId);

    ProductOrder updateOrderStatus(String id, String status);

    List<ProductOrder> getAllOrder();

    ProductOrder getOrdersByOrderId(String orderId);

    Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
