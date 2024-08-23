package com.blog.service;

import com.blog.playload.CategoryDto;
import com.blog.playload.PostDto;
import org.springframework.http.ResponseEntity;


import java.util.List;


public interface CategoryService {

    ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto);
    ResponseEntity<CategoryDto> updateCategory(CategoryDto category, String categoryName);
    CategoryDto getCategoryByName(String categoryName);
    List<CategoryDto> getAllCategory();
    String deleteUserById(String categoryName);
    String deleteAll();
    List<CategoryDto> searchCategory(String keyword);


    List<CategoryDto> searchByPost(String keyword);
}
