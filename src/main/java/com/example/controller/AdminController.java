package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	public String AddCategory(Model m){
		m.addAttribute("categorys",categoryService.getAllCategory());
		return "admin/category";
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file ,
			HttpSession session) throws IOException {
		
		String imageName = file !=null ? file.getOriginalFilename() : "default.jpg";
		category.setImageName(imageName);
		
	    Boolean existCategory = categoryService.existCategory(category.getName());

	    if (existCategory) {
	        session.setAttribute("errorMsg", "Category already exists");
	    }else {
	    	Category savedCategory = categoryService.saveCategory(category);
	    	
	    		if (ObjectUtils.isEmpty(savedCategory)) {
	    			
	    			session.setAttribute("errorMsg", "Failed to save category");
	    			
	    			}else {

	    			File SaveFile = new ClassPathResource("static/img").getFile();
	    			
	    				Path path = Paths.get(SaveFile.getAbsolutePath()+File.separator + "category_img" + File.separator 
	    						+ file.getOriginalFilename());
	    				
	    				System.out.println(path);
	    				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING );
//	Image Path find it here -- C:\Users\HP\Documents\workspace-spring-tools-for-eclipse-4.30.0.RELEASE\Shopping_Cart\target\classes\static\img\category_img\footer01.jpeg
	    				
	    				session.setAttribute("successMsg", "Category saved successfully");
	    			}
	    }
	    return "redirect:/admin/AddCategory";
	}
	
	
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCtegory(@PathVariable int id, HttpSession session) {
		
		Boolean deleteCategory = categoryService.deleteCategory(id);
		
		if(deleteCategory) {
			session.setAttribute("successMsg", "category delete successfuly");
		}else {
			session.setAttribute("errorMsg", "Operation faild !category not delete");
		}
		return  "redirect:/admin/AddCategory";
	}
	
	
	@GetMapping("/loadEditCategory/{id}")
	public String LoadEditCategory(@PathVariable int id, Model m) {
		m.addAttribute("category",categoryService.getCategoryById(id));
		return "/admin/edit_category";
	}
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category,@RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException{
		
		Category oldCategory = categoryService.getCategoryById(category.getId());
		String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(category)) {
		    oldCategory.setName(category.getName());
		    oldCategory.setIsActive(category.getIsActive());
		    oldCategory.setImageName(imageName);
		}

		Category updateCategory = categoryService.saveCategory(oldCategory);

		// ✅ Save image if uploaded
		if (!file.isEmpty()) {
		    try {
		        File saveFile = new ClassPathResource("static/img/category_img").getFile();
		        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
		        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		    } catch (IOException e) {
		        e.printStackTrace();
		        session.setAttribute("errorMsg", "Image upload failed");
		        return "redirect:/admin/loadEditCategory/" + category.getId();
		    }
		}

		// ✅ Set success or error message
		if (!ObjectUtils.isEmpty(updateCategory)) {
		    session.setAttribute("successMsg", "Category update success");
		} else {
		    session.setAttribute("errorMsg", "Category not updated");
		}

		return "redirect:/admin/loadEditCategory/" + category.getId();
	}
	
}
