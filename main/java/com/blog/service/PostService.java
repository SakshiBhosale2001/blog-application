package com.blog.service;

import com.blog.playload.CategoryDto;
import com.blog.playload.PostDto;
import com.blog.playload.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface PostService {


    ResponseEntity<PostDto> createPost(PostDto postDto, MultipartFile multipartFile) throws IOException;
    ResponseEntity<PostDto> updatePost(PostDto postDto, MultipartFile multipartFile,String title) throws IOException;

    ResponseEntity<PostDto> getPostByTitle(String title);
    Page<PostDto> getAllPost(Pageable pageable);
    String deletePostByTitle(String title);
    String deleteAll();
    List<PostDto> getPostByUserName(String email);
    List<PostDto> getPostByCategory(String categoryName);
    UserDto getUserByPost(String title);
    CategoryDto getCategoryByPost(String title);
    List<PostDto> searchPosts(String keyword);

    List<PostDto> searchByCategory(String keyword);

    List<PostDto> searchByUser(String keyword);
}
