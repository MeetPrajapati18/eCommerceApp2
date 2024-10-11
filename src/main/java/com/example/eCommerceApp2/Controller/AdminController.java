package com.example.eCommerceApp2.Controller;

import com.example.eCommerceApp2.model.Category;
import com.example.eCommerceApp2.model.Product;
import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.service.CategoryService;
import com.example.eCommerceApp2.service.ProductService;
import com.example.eCommerceApp2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

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

    @GetMapping({"", "/"})
    public String index() {
        return "admin/index";
    }

    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categories",categoryService.getAllCategories());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        // Check if a file was uploaded, otherwise use a default image
        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        // Check if the category already exists
        if (categoryService.existsCategory(category.getName())) {
            session.setAttribute("errorMsg", "Category Name already exists.");
        } else {
            Category saveCategory = categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "Not Saved! Internal server error!");
            } else {
                // Define an absolute path outside the classpath (e.g., a folder inside your project root)
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/category_img";
                File saveFile = new File(uploadDir);

                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }

                Path filePath = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("successMsg", "Saved Successfully");
            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable String id, HttpSession session) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            session.setAttribute("successMsg", "Category deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Category not found");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable String id, Model m){
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session){

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName =  file.isEmpty() ? oldCategory.getImageName(): file.getOriginalFilename();

        if(!ObjectUtils.isEmpty(category)){
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }
        Category updateCategory = categoryService.saveCategory(oldCategory);
        if(!ObjectUtils.isEmpty(updateCategory)){
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
            session.setAttribute("successMsg","Category Updated Successfully.");
        } else{
            session.setAttribute("errorMsg","Sorry Something went Wrong.");
        }

        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    //Product Section
    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m){
        List<Category> categories = categoryService.getAllCategories();
        m.addAttribute("categories",categories);
        return "admin/add_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image, HttpSession session){
        String imageName = image.isEmpty()?"default.jpg": image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0.0);
        product.setDiscountPrice(product.getPrice());
        Product saveProduct = productService.saveProduct(product);

        if(!ObjectUtils.isEmpty(saveProduct)){

            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/product_img";
            File saveFile = new File(uploadDir);
            Path filePath = Paths.get(saveFile.getAbsolutePath() + File.separator + image.getOriginalFilename());
            try {
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            session.setAttribute("successMsg","Product Saved Successfully.");
        } else {
            session.setAttribute("errorMsg","Sorry, Something went wrong on server.");
        }
        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m){
        m.addAttribute("products",productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable String id, HttpSession session){
        Boolean deleteProduct = productService.deleteProduct(id);
        if(deleteProduct){
            session.setAttribute("successMsg","Your product has been deleted successfully.");
        } else {
            session.setAttribute("errorMsg","Sorry  something went wrong in server.");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable String id, Model m){
        m.addAttribute("product",productService.getProductById(id));
        m.addAttribute("categories",categoryService.getAllCategories());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image, HttpSession session ,Model m){

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("errorMsg", "invalid Discount");
        } else {
            Product updateProduct = productService.updateProduct(product, image, session);
            if (!ObjectUtils.isEmpty(updateProduct)) {
                session.setAttribute("successMsg", "Product has been updated successfully.");
            } else {
                session.setAttribute("errorMsg", "Something wrong on server");
            }
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model m){
        List<UserEntity> users = userService.getUsers("ROLE_USER");

        m.addAttribute("users",users);
        return "/admin/users";
    }
}

