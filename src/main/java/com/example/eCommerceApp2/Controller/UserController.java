package com.example.eCommerceApp2.Controller;


import com.example.eCommerceApp2.model.*;
import com.example.eCommerceApp2.service.CartService;
import com.example.eCommerceApp2.service.OrderService;
import com.example.eCommerceApp2.service.UserService;
import com.example.eCommerceApp2.service.CategoryService;
import com.example.eCommerceApp2.util.CommonUtil;
import com.example.eCommerceApp2.util.OrderStatus;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
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

    @Autowired
    private CommonUtil commonUtil;

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
        if(!carts.isEmpty()){
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = 1.13 * (carts.get(carts.size()-1).getTotalOrderPrice());
            m.addAttribute("orderPrice",orderPrice);
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p){
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
    public String updateStatus(@RequestParam String id, @RequestParam int st, HttpSession session){

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderStatus:values){
            if (orderStatus.getId().equals(st)){
                status = orderStatus.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);
        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(!ObjectUtils.isEmpty(updateOrder)){
            session.setAttribute("successMsg", "Order status updated successfully.");
        } else{
            session.setAttribute("errorMsg","Sorry,Something went wrong.");
        }

        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile(Model m, Principal p){
        UserEntity userEntity = getLoggedInUserDetails(p);
        m.addAttribute("userEntity", userEntity);
        return "/user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserEntity user, @RequestParam MultipartFile img, HttpSession session){
        UserEntity updateUserProfile = userService.updateUserProfile(user, img);
        if(ObjectUtils.isEmpty(updateUserProfile)){
            session.setAttribute("errorMsg", "Sorry something went wrong.");
        } else{
            session.setAttribute("successMsg", "Hurrah!, Profile updated successfully.");
        }
        return "redirect:/user/profile";
    }

}
