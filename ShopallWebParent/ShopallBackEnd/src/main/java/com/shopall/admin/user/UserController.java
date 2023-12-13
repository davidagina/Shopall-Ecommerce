package com.shopall.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopall.common.entity.Role;
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
		List<Role> listRoles = service.listRoles();
	
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		service.save(user);
		System.out.println(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		return "redirect:/users";
	}
	
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
		try {
		 User user = service.getUserById(id);
		 model.addAttribute("user", user);
		 model.addAttribute("pageTitle", "Edit User" + " (ID: " + id +")");
		 List<Role> listRoles = service.listRoles();
		 
		 model.addAttribute("listRoles", listRoles);
		 
		 return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
		try {
			service.deleteUserById(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully.");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/users";
	}
}
