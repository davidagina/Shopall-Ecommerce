package com.shopall.admin.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopall.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer>{

	
}
