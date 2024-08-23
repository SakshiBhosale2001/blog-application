package com.blog.repository;

import com.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,String> {
    List<Category> findByCategoryNameContaining(String keyword);
    List<Category> findByPost_TitleContaining(String keyword);
}
