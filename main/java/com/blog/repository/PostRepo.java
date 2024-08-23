package com.blog.repository;

import com.blog.entity.Category;
import com.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,String> {

    List<Post> findByTitleContaining(String keyword);
    List<Post> findByCategory_CategoryNameContaining(String keyword);
    List<Post> findByUser_UserNameContaining(String keyword);
}
