package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductRepository productRespository;
	
	@Override
	public Product saveProduct(Product product) {
		return productRespository.save(product);
	}

	@Override
	public List<Product> getAllProduct() {
		return productRespository.findAll();
	}

}
