package com.blog.controller;

import com.blog.playload.ApiResponse;
import com.blog.playload.CategoryDto;

import com.blog.service.impl.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto)
    {
        return this.categoryService.createCategory(categoryDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{name}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("name") String categoryName)
    {
        return this.categoryService.updateCategory(categoryDto,categoryName);
    }

    @GetMapping("/categoryDetails/{name}")
    public ResponseEntity<CategoryDto> getCategory( @PathVariable("name") String categoryName)
    {
        return ResponseEntity.ok(this.categoryService.getCategoryByName(categoryName));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDto>> getAllCategory()
    {
        return ResponseEntity.ok(this.categoryService.getAllCategory());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<ApiResponse> deleteUser( @PathVariable("name") String categoryName)
    {

        return ResponseEntity.ok(new ApiResponse(this.categoryService.deleteUserById(categoryName),true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/allCategories")
    public ResponseEntity<String> deleteAllCategory()
    {
        return ResponseEntity.ok(this.categoryService.deleteAll());
    }

    @GetMapping("/search/category/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.categoryService.searchCategory(keyword));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/post/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchByPost(@PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.categoryService.searchByPost(keyword));
    }
}
