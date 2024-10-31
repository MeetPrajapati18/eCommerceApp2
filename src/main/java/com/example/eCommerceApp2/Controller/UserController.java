package com.example.eCommerceApp2.Controller;


import com.example.eCommerceApp2.model.*;
import com.example.eCommerceApp2.service.CartService;
import com.example.eCommerceApp2.service.OrderService;
import com.example.eCommerceApp2.service.UserService;
import com.example.eCommerceApp2.service.CategoryService;
import com.example.eCommerceApp2.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p != null){
            String email = p.getName();
            UserEntity userByEmail = userService.getUserByEmail(email);
            m.addAttribute("user",userByEmail);
            Integer countCart = cartService.getCountCart(userByEmail.getId());
            m.addAttribute("countCart", countCart);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categories",allActiveCategory);
    }

    @GetMapping("")
    public String home() {
        return "user/home";
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam String productId, @RequestParam String userId, HttpSession session){
        Cart saveCart = cartService.saveCart(productId, userId);

        if (ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("successMsg", "Product has been added to cart.");
        } else{
            session.setAttribute("errorMsg", "Sorry, Failed to add product into the cart.");
        }
        return "redirect:/product/"+productId;
    }

    private UserEntity getLoggedInUserDetails(Principal p){
        String email = p.getName();
        UserEntity userEntity = userService.getUserByEmail(email);
        return userEntity;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m){
        UserEntity user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if(carts.size() > 0){
            Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String symbol, @RequestParam String cartId){
        cartService.updateQuantity(symbol,cartId);
        return "redirect:/user/cart";
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m){
        UserEntity user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if(carts.size() > 0){
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = 1.13 * (carts.get(carts.size()-1).getTotalOrderPrice() + 5);
            m.addAttribute("orderPrice",orderPrice);
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p){
//        System.out.println(request);
        UserEntity user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);
        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String loadSuccess(){
        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m, Principal p){
        UserEntity loginUser = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrderByUser(loginUser.getId());
        m.addAttribute("orders",orders);
        return "/user/my_order";
    }

    @GetMapping("/update-status")
    public String updateStatus(@RequestParam String id, @RequestParam String st){

        OrderStatus[] values = OrderStatus.values();
        System.out.println("Values:" + values);
        return "redirect:/user/orders";
    }
}
