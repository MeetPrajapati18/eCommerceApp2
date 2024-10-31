package com.example.eCommerceApp2.service.Impl;

import com.example.eCommerceApp2.model.Cart;
import com.example.eCommerceApp2.model.OrderAddress;
import com.example.eCommerceApp2.model.OrderRequest;
import com.example.eCommerceApp2.model.ProductOrder;
import com.example.eCommerceApp2.repository.CartRepository;
import com.example.eCommerceApp2.repository.ProductOrderRepository;
import com.example.eCommerceApp2.service.OrderService;
import com.example.eCommerceApp2.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public void saveOrder(String userId, OrderRequest orderRequest) {

        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts){

            ProductOrder order = new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstname(orderRequest.getFirstName());
            address.setLastname(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);
            orderRepository.save(order);
        }
    }

    @Override
    public List<ProductOrder> getOrderByUser(String userId) {
        List<ProductOrder> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public Boolean updateOrderStatus(String id, String status) {
        Optional<ProductOrder> findById = orderRepository.findById(id);
        if (findById.isPresent()){
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            orderRepository.save(productOrder);
            return true;
        }
        return false;
    }
}
