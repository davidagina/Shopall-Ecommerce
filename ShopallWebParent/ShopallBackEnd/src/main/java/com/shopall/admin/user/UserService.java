package com.shopall.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopall.common.entity.Role;
import com.shopall.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	private RoleRepository roleRepo;
	
	public List<User> usersList(){
		return userRepo.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}
}
