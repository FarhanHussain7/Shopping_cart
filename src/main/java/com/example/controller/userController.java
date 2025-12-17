package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Category;
import com.example.model.UserDtls;
import com.example.service.CategoryService;
import com.example.service.UserService;

@Controller
@RequestMapping("/user")
public class userController {
	
	@GetMapping("/")
	public String home() {
		return "user/home";
	}

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public void getUserDetails(Principal p,Model m) {
		if(p!=null) {
			String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		m.addAttribute("user", userDtls);
		}
	
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
		
	}
	
}
