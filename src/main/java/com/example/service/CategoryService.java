package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Category;

@Service
public interface CategoryService {

	public Category saveCategory(Category category);
	
	public Boolean existCategory(String name);
	
	public List<Category> getAllCategory();
	
	public Boolean deleteCategory(int id);
	
	public Category getCategoryById(int id);
}
