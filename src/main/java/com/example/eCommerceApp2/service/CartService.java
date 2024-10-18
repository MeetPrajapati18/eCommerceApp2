package com.example.eCommerceApp2.service;

import com.example.eCommerceApp2.model.Cart;

import java.util.List;

public interface CartService {

    Cart saveCart(String productId, String userId);

    List<Cart> getCartsByUser(String userId);

    Integer getCountCart(String userid);

    void updateQuantity(String symbol, String cartId);
}
