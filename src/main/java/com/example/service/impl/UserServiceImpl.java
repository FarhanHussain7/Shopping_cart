package com.example.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.UserDtls;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.util.AppConstant;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDtls saveUser(UserDtls user) {
		user.setRole("USER");
		user.setEnabled(true);
		user.setAccountNonLocked(true);
		user.setFailedAttempt(0);
		user.setLockTime(null);
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		UserDtls saveUser = userRepository.save(user);
		return saveUser;
	}

	
	@Override
	public UserDtls getUserByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDtls> getUsers(String role) {
	return	userRepository.findByRole(role);
		
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		
	Optional<UserDtls> findByuser =	userRepository.findById(id);
		if(findByuser.isPresent()) {
		UserDtls userDtls=findByuser.get();
		userDtls.setEnabled(status);
		userRepository.save(userDtls);
		return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttempt(UserDtls user) {
	int attempt = user.getFailedAttempt() +1;
		user.setFailedAttempt(attempt);
		userRepository.save(user);
	}

	@Override
	public void userAccountLock(UserDtls user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new java.util.Date());
		userRepository.save(user);
	}

	@Override
	public void resetAttempt(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean unloackAccountTimeExpired(UserDtls user) {
		long lockTime = user.getLockTime().getTime();
		long unLockTime = lockTime + AppConstant.UNLOCKED_DURATION_TIME;
		
		long currentTime = System.currentTimeMillis();
		
		if(unLockTime<currentTime) {
			user.setAccountNonLocked(true);
			user.setFailedAttempt(0);
			user.setLockTime(null);
			userRepository.save(user);
			return true;
		}
		return false;
	}

}
