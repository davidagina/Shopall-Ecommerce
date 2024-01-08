package com.shopall.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.shopall.admin.user.UserNotFoundException;
import com.shopall.common.entity.Category;


@Controller
public class CategoryController {
	
	@Autowired
	CategoryService service;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> categoryList = service.listAll();
		model.addAttribute("categoryList", categoryList);
		return "category/categories";
	}
	
	
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable int pageNum, Model model, @Param("sortField") 
	String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		
		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Order: " + sortDir);
		
		Page<Category> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Category> listCategories = page.getContent();
		
		@SuppressWarnings("static-access")
		long startCount = (pageNum - 1) * service.CATEGORIES_PER_PAGE + 1;
		@SuppressWarnings("static-access")
		long endCount = startCount + service.CATEGORIES_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("categoryList", listCategories);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "category/categories";
	}
	
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		return "category/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") 
	MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category savedCategory = service.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (category.getImage().isEmpty())
				category.setImage(null);
			service.save(category);
		}
		
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
		return getRedirectURLtoAffectedUser(category);
	}
	
	
	private String getRedirectURLtoAffectedUser(Category category) {
		String name = category.getName();
		return "redirect:/categories/page/1?sortField=name&sortDir=asc&keyword=" + name;
	}
	
	
	@GetMapping("/category/edit/{id}")
	public String editCategory(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException, CategoryNotFoundException {
		
		try {
			Category category = service.getCategoryById(id);
			List<Category> listCategories = service.listCategoriesUsedInForm();
			
			model.addAttribute("category", category);
			model.addAttribute("categoryList", listCategories);
			model.addAttribute("pageTitle", "Edit User" + " (ID: " + id +")");
			
			return "/category/category_form";
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addAttribute("message", ex.getMessage());
			return "redirect:/category/categories";
		}
		 
		 
		 
	}

}
