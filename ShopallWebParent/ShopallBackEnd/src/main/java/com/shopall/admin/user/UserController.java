package com.shopall.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopall.admin.FileUploadUtil;
import com.shopall.common.entity.Role;
import com.shopall.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage(Model model) {
		List<User> listUsers = service.usersList();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable Integer pageNum, Model model) {
		Page<User> page = service.listByPage(pageNum);
		List<User> listUsers = page.getContent();
		
		System.out.println("Total pages: " + pageNum);
		System.out.println("Total elements: " + page.getTotalElements());
		System.out.println("Total pages: " + page.getTotalPages());
		
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
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
		
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.save(user);
		}
		
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
		
		String uploadDir = "user-photos/" + id;
		try {
			service.deleteUserById(id);
			FileUploadUtil.cleanDir(uploadDir);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully.");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateEnabled(@PathVariable Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		redirectAttributes.addFlashAttribute("message", "The user with ID: " + id + " has been " + status +".");
		return "redirect:/users";
	}
	
	
}
