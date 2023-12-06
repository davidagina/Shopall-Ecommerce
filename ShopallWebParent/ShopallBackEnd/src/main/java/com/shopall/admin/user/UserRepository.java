package com.shopall.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopall.common.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User getUserByEmail(String email);
	
	
}
