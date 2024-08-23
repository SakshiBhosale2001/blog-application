package com.blog.controller;
import com.blog.playload.ApiResponse;
import com.blog.playload.CategoryDto;
import com.blog.playload.PostDto;
import com.blog.playload.UserDto;
import com.blog.service.impl.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity<PostDto> create(@Valid @RequestPart("postDto") PostDto postDto
            ,@RequestParam("image") MultipartFile multipartFile) throws IOException
    {
        return postService.createPost(postDto,multipartFile);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update/{title}")
    public ResponseEntity<PostDto> update(@Valid @RequestPart("postDto") PostDto postDto
            , @RequestParam("image") MultipartFile multipartFile, @PathVariable String title) throws IOException
    {
        return postService.updatePost(postDto,multipartFile,title);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/postDetails/{title}")
    public ResponseEntity<PostDto> getPost( @PathVariable("title") String title)
    {
        return this.postService.getPostByTitle(title);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getAll")
    public Page<PostDto> getAllPosts(Pageable pageable)
    {
        return this.postService.getAllPost(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{title}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("title") String title)
    {

        return ResponseEntity.ok(new ApiResponse(this.postService.deletePostByTitle(title),true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllPosts()
    {
        return ResponseEntity.ok(this.postService.deleteAll());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/postByUserName/{userName}")
    public ResponseEntity<List<PostDto>> getPostByUserId( @PathVariable("userName") String name)
    {
        return ResponseEntity.ok(this.postService.getPostByUserName(name));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/postByCategoryId/{name}")
    public ResponseEntity<List<PostDto>> getPostBycategoryId( @PathVariable("name") String categoryName)
    {
        return ResponseEntity.ok(this.postService.getPostByCategory(categoryName));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/post/{title}")
    public ResponseEntity<CategoryDto> getCategory( @PathVariable("title") String title)
    {
        return ResponseEntity.ok(this.postService.getCategoryByPost(title));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{title}")
    public ResponseEntity<UserDto> getUser(@PathVariable("title") String title)
    {
        return ResponseEntity.ok(this.postService.getUserByPost(title));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchPost( @PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.postService.searchPosts(keyword));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/byCategory/{keyword}")
    public ResponseEntity<List<PostDto>> searchByCategory( @PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.postService.searchByCategory(keyword));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/byUser/{keyword}")
    public ResponseEntity<List<PostDto>> searchByUser( @PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.postService.searchByUser(keyword));
    }
}
