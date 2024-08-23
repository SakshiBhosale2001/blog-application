package com.blog.controller;
import com.blog.playload.*;
import com.blog.service.impl.CommentService;
import com.blog.service.impl.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto commentDto)
    {
        return commentService.createComment(commentDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CommentDto> update(@Valid @RequestBody CommentDto commentDto
            ,@PathVariable("id") Integer id)
    {
        return commentService.updateComment(commentDto,id);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/commentDetails/{id}")
    public ResponseEntity<CommentDto> getPost( @PathVariable("id") Integer id)
    {
        return ResponseEntity.ok(this.commentService.getCommentById(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getAll")
    public Page<CommentDto> getByPost(Pageable pageable)
    {

        return this.commentService.getAllComment(pageable);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/getByUser/{name}")
    public List<CommentDto> getByUser(@PathVariable("name") String userName)
    {
        return this.commentService.getCommentByUser(userName);
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("id") Integer id)
    {

        return ResponseEntity.ok(new ApiResponse(this.commentService.deleteCommentById(id),true));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllPosts(@PathVariable("title") String title)
    {
        return ResponseEntity.ok(this.commentService.deleteAllCommentsByPost(title));
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/comment/{keyword}")
    public ResponseEntity<List<CommentDto>> searchComment( @PathVariable("keyword") String keyword,Pageable pageable)
    {
        return ResponseEntity.ok(this.commentService.searchComment(keyword,pageable));
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/post/{keyword}")
    public ResponseEntity<List<CommentDto>> searchByPost( @PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.commentService.searchByPost(keyword));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/user/{keyword}")
    public ResponseEntity<List<CommentDto>> searchByUser( @PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.commentService.searchByUser(keyword));
    }

}
