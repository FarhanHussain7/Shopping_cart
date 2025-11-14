package com.example.service;

import java.util.List;

import com.example.model.Product;

public interface ProductService {
	public Product saveProduct(Product product);
	
	public List<Product> getAllProduct();
}
