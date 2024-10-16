package com.example.eCommerceApp2.Controller;

import com.example.eCommerceApp2.model.Category;
import com.example.eCommerceApp2.model.Product;
import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.service.CategoryService;
import com.example.eCommerceApp2.service.ProductService;
import com.example.eCommerceApp2.service.UserService;
import com.example.eCommerceApp2.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private JavaMailSender mailSender;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p != null){
            String email = p.getName();
            UserEntity userByEmail = userService.getUserByEmail(email);
            m.addAttribute("user",userByEmail);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categories",allActiveCategory);
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/product")
    public String product(Model m, @RequestParam(value = "category", defaultValue = "") String category){
        System.out.println("Category = " + category);
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProduct(category);
        m.addAttribute("paramValue", category);
        m.addAttribute("categories",categories);
        m.addAttribute("products",products);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String view_product(@PathVariable String id, Model m){
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "view_product";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserEntity user, @RequestParam("img") MultipartFile file, HttpSession session){
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setImage(imageName);
        UserEntity userDetail = userService.saveUser(user);

        if(!ObjectUtils.isEmpty(userDetail)){
            if (!file.isEmpty()){
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/profile_img";
                File saveFile = new File(uploadDir);
                Path filePath = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                try {
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            session.setAttribute("successMsg","You have benn registered Successfully.");
        } else{
            session.setAttribute("errorMsg","Sorry Something went Wrong.");
        }
        return "redirect:/register";
    }

//    @GetMapping("/category")
//    public String getActiveCategory(Model m){
//
//        return "";
//    }

    //Forget Password Controller code

    @GetMapping("/forgot-password")
    public String showForgotPassword(){
        return "forgot_password";
    }



    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        UserEntity userByEmail = userService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(userByEmail)){
            session.setAttribute("errorMsg", "Invalid or null email address");
        } else {

            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);

            // Generate URL
            String url = CommonUtil.generateURL(request) + "/reset-password?token=" + resetToken;
            Boolean sendMail = CommonUtil.sendMail(mailSender, url, email);

            if (sendMail) {
                session.setAttribute("successMsg", "Mail has been sent successfully, please check your mailbox.");
            } else {
                session.setAttribute("errorMsg", "Something went wrong on the server.");
            }
        }
        return "redirect:/forgot-password";
    }


    @GetMapping("/reset-password")
    public String showResetPassword(){
        return "reset_password";
    }
}














