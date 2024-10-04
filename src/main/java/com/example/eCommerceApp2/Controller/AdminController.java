package com.example.eCommerceApp2.Controller;

import com.example.eCommerceApp2.model.Category;
import com.example.eCommerceApp2.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private CategoryService categoryService;

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(){
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(){
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session){

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        if(categoryService.existCategory(category.getName())){
            session.setAttribute("errorMsg","Category Name already exists.");
        } else{
            Category saveCategory = categoryService.saveCategory(category);
            if(ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("errorMsg","Not Saved! internal server error!");
            } else{
                session.setAttribute("successMsg","Saved Successfully");
            }
        }

        categoryService.saveCategory(category);
        return "redirect:/category";
    }
}
