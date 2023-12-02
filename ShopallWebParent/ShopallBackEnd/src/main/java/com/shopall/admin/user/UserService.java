package com.shopall.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopall.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	public List<User> usersList(){
		return repo.findAll();
	}
}
