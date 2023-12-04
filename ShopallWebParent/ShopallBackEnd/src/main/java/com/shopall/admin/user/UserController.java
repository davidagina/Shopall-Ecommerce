package com.shopall.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopall.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String usersList(Model model) {
		List<User> listUsers = service.usersList();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("users/new")
	public String newUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "user_form";
	}
}
