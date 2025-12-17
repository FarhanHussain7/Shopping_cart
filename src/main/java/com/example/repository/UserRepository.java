package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
			
	public UserDtls findByEmail(String email);

	public List<UserDtls> findByRole(String role);
}
