package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Category;
import com.example.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	public CategoryService categoryService;
	
	
	@GetMapping("/")
	public String index(){
		return "admin/index";
	}
	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct(){
		return "admin/add_product";
	}
	
	@GetMapping("/AddCategory")
	public String AddCategory(){
		return "admin/category";
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, HttpSession session) {

	    Boolean existCategory = categoryService.existCategory(category.getName());

	    if (existCategory) {
	        session.setAttribute("errorMsg", "Category already exists");
//	        return "redirect:/admin/AddCategory";
	    }else {

	    	Category savedCategory = categoryService.saveCategory(category);
	    		if (ObjectUtils.isEmpty(savedCategory)) {
	    			session.setAttribute("errorMsg", "Failed to save category");
//	       				 return "redirect:/admin/AddCategory";
	    			}else {

	    				session.setAttribute("successMsg", "Category saved successfully");
	    			}
	    }
	    return "redirect:/admin/AddCategory";
	}
}
