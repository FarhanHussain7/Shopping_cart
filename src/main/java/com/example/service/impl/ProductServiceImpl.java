package com.example.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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

	@Override
	public Boolean deleteProduct(Integer id) {
	Product product = productRespository.findById(id).orElse(null);
	
	if(!ObjectUtils.isEmpty(product)) {
		productRespository.delete(product);
		return true;
	}
		return false;
	}

	@Override
	public Product getProductById(Integer id) {
	 Product product = productRespository.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile image) {
		
		Product dbproduct = getProductById(product.getId());
		
		String imageName = image.isEmpty() ? dbproduct.getImage(): image.getOriginalFilename();
		
		dbproduct.setTitle(product.getTitle());
		dbproduct.setDescription(product.getDescription());
		dbproduct.setCategory(product.getCategory());
		dbproduct.setPrice(product.getPrice());
		dbproduct.setStock(product.getStock());
		dbproduct.setIsActive(product.getIsActive());
		
		dbproduct.setDiscount(product.getDiscount());
	Double discount	= product.getPrice()*(product.getDiscount()/100.0);
		
	Double discountPrice = product.getPrice()-discount;
	
		dbproduct.setDiscountPrice(discountPrice);
		Product updateproduct = productRespository.save(dbproduct);
		
		if(!ObjectUtils.isEmpty(updateproduct)) {
			if(!image.isEmpty()) {
				try {
				File saveFile = new ClassPathResource("static/img").getFile();
				
		        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator+ "product_img"+ File.separator + image.getOriginalFilename());
		        
		        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return product;
		}
		
		return null;
	}

	@Override
	public List<Product> getAllActiveProduct(String category) {
		List<Product> products = null;
		if(ObjectUtils.isEmpty(category)) {
			products = productRespository.findByIsActiveTrue();
		}else {
			products = productRespository.findByCategory(category);
		}
	
		return products;
	}
}
