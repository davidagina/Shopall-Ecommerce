package com.shopall.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopall.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer>{

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories();
	
	
	public Category findByName(String name);
	
	public Category findByAlias(String alias);
	
	@Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ', c.name, ' ',"
			+ " c.alias) LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);
}
