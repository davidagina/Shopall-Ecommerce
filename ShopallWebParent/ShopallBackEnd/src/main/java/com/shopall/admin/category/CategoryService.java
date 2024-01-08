package com.shopall.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopall.common.entity.Category;

@Service
public class CategoryService {
	
	public static final int CATEGORIES_PER_PAGE = 4;
	
	@Autowired
	private CategoryRepository repo;
	
	
	
	public List<Category> listAll(){
		List<Category> rootCategories = repo.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}
	
	
	
	public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);
	}
	
	
	
	public List<Category> listHierarchicalCategories(List<Category> rootCategories){
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}
			
		}
		
		return hierarchicalCategories;
	}
	
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, 
			Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
		}
	}
	
	
	public Category save(Category category) {
		return repo.save(category);
	}
	
	public List<Category> listCategoriesUsedInForm(){
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = repo.findAll();
		
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> children = category.getChildren();
				
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}
		
		return categoriesUsedInForm;
	}
	
	
	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren(); 
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}
	
	
	public Category getCategoryById(Integer id) throws CategoryNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			// TODO: handle exception
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
		
	}

}
