package com.blog.service.impl;
import com.blog.entity.Category;
import com.blog.exception.ResourceNotFoundException;
import com.blog.playload.CategoryDto;
import com.blog.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements com.blog.service.CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {

        return new ResponseEntity<>
                (this.modelMapper.map(this.categoryRepo.save(modelMapper.map
                                (categoryDto,Category.class)),CategoryDto.class)
                        , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(CategoryDto categoryDto, String categoryName)
    {
        Category category=this.categoryRepo.findById(categoryName)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryName",categoryName));

        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        return  new ResponseEntity<>
                (this.modelMapper.map(this.categoryRepo.save(category),CategoryDto.class)
                        , HttpStatus.CREATED);
    }

    @Override
    public CategoryDto getCategoryByName(String categoryName) {

        return  modelMapper.map(this.categoryRepo.findById(categoryName)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryName",categoryName))
                ,CategoryDto.class);

    }

    @Override
    public List<CategoryDto> getAllCategory() {

        return this.categoryRepo.findAll().stream()
                .map(category->modelMapper.map(category,CategoryDto.class)).toList();
    }

    @Override
    public String deleteUserById(String categoryName)
    {
        this.categoryRepo.findById(categoryName)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryName",categoryName));

        return "Category with Id :"+categoryName+" Deleted Successfully..!!";
    }

    @Override
    public String deleteAll() {

        try {

            this.categoryRepo.deleteAll();
            return "Deleted All Categories Successfully..!!";

        }
        catch (Exception e)
        {
            return e.getLocalizedMessage();
        }
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        return this.categoryRepo.findByCategoryNameContaining(keyword).stream()
                .map(category->modelMapper.map(category,CategoryDto.class)).toList();

    }

    @Override
    public List<CategoryDto> searchByPost(String keyword) {
        return this.categoryRepo.findByPost_TitleContaining(keyword).stream()
                .map(category->modelMapper.map(category,CategoryDto.class)).toList();

    }

}


