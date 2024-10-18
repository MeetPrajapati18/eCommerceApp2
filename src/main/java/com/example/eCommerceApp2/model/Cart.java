package com.example.eCommerceApp2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;


public class Cart {

    @Id
    private String Id;

    private UserEntity user;

    private Product product;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
