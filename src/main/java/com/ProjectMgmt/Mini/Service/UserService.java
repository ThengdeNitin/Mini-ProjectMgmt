package com.ProjectMgmt.Mini.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ProjectMgmt.Mini.Entity.User;
import com.ProjectMgmt.Mini.Repository.UserRepository;

@Service
public class UserService {

	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User registerUser(String username, String password) {
		if(userRepository.findByUsername(username).isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		User user = User.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.build();
		return userRepository.save(user);
	}
	
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
