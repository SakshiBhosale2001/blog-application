package com.blog.service;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.playload.CommentDto;
import com.blog.playload.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface CommentService {

    ResponseEntity<CommentDto> createComment(CommentDto commentDto);
    ResponseEntity<CommentDto> updateComment(CommentDto commentDto,Integer id);

    CommentDto getCommentById(Integer id);
    List<CommentDto> getCommentByUser(String userName);
    Page<CommentDto> getAllComment(Pageable pageable);

    String deleteCommentById(Integer id);
    String deleteAllCommentsByPost(String title);

    List<CommentDto> searchComment(String keyword,Pageable pageable);
    List<CommentDto> searchByUser(String keyword);
    List<CommentDto> searchByPost(String keyword);
}
