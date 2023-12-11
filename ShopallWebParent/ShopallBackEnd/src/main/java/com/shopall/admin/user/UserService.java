package com.shopall.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopall.common.entity.Role;
import com.shopall.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> usersList(){
		return userRepo.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {
		encodePassword(user);
		userRepo.save(user);
		
	}
	
	public void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		return userByEmail == null;
	}
	
	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			User user = userRepo.findById(id).get();
			return user;
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
	}
}
