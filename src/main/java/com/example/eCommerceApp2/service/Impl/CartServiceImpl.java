package com.example.eCommerceApp2.service.Impl;

import com.example.eCommerceApp2.model.Cart;
import com.example.eCommerceApp2.model.Product;
import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.repository.CartRepository;
import com.example.eCommerceApp2.repository.ProductRepository;
import com.example.eCommerceApp2.repository.UserRepository;
import com.example.eCommerceApp2.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(String productId, String userId) {
        UserEntity userEntity = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)){
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(userEntity);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());
        } else{
            cart = cartStatus;
            cart.setQuantity(cartStatus.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }
        Cart saveCart = cartRepository.save(cart);
        return null;
    }

    @Override
    public List<Cart> getCartsByUser(String userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for( Cart c : carts){
            Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
            totalPrice = Double.valueOf(String.format("%.2f", totalPrice));
            c.setTotalPrice(totalPrice);
            totalOrderPrice += totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
            updateCarts.add(c);
        }
//        carts.get(0).setTotalPrice(totalPrice);
        return updateCarts;
    }

    @Override
    public Integer getCountCart(String userid) {
        Integer countByUserid;
        countByUserid = cartRepository.countByUserId(userid);
        return countByUserid;
    }

    @Override
    public void updateQuantity(String symbol, String cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);

        if (!optionalCart.isPresent()) {
            // Handle the case where the cart is not found, e.g., log an error or throw an exception
            throw new NoSuchElementException("No cart found with ID: " + cartId);
        }

        Cart cart = optionalCart.get();
        Integer updateQuantity;

        if (symbol.equals("decrease")) {
            updateQuantity = cart.getQuantity() - 1;
            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else{
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }
        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }
    }

}
