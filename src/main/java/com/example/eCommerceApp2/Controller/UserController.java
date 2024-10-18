package com.example.eCommerceApp2.Controller;


import com.example.eCommerceApp2.model.Cart;
import com.example.eCommerceApp2.model.Category;
import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.service.CartService;
import com.example.eCommerceApp2.service.UserService;
import com.example.eCommerceApp2.service.CategoryService;
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
}
