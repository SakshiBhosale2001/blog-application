package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.playload.CommentDto;
import com.blog.repository.CommentRepo;
import com.blog.repository.PostRepo;
import com.blog.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService implements com.blog.service.CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ResponseEntity<CommentDto> createComment(CommentDto commentDto) {
        String title=commentDto.getPostDto().getTitle();
        String email=commentDto.getUserDto().getEmail();

        Post post=postRepo.findById(title)
                .orElseThrow(()-> new ResourceNotFoundException("Post","title",title));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User","name",email));

        Comment comment=modelMapper.map(commentDto,Comment.class);
        comment.setCommentedDate(new Date());
        comment.setPost(post);
        comment.setUser(user);

        return ResponseEntity.status(201)
                .body(modelMapper.map(commentRepo.save(comment),CommentDto.class));
    }

    @Override
    public ResponseEntity<CommentDto> updateComment(CommentDto commentDto,Integer id)
    {
        String title=commentDto.getPostDto().getTitle();
        String email=commentDto.getUserDto().getEmail();

        Post post=postRepo.findById(title)
                .orElseThrow(()-> new ResourceNotFoundException("Post","title",title));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User","name",email));

        Comment comment=commentRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","id",id.toString()));

        comment.setComment(commentDto.getComment());
        comment.setId(id);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentedDate(new Date());

        return ResponseEntity.ok(modelMapper.map(commentRepo.save(comment),CommentDto.class));
    }

    @Override
    public CommentDto getCommentById(Integer id)
    {
        return modelMapper.map(commentRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","id",id.toString()))
                ,CommentDto.class);

    }


    @Override
    public List<CommentDto> getCommentByUser(String userName) {

        return commentRepo.findByUser_UserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "name", userName))
                .stream().map(comment -> modelMapper.map(comment, CommentDto.class)).toList();
    }

    @Override
    public Page<CommentDto> getAllComment(Pageable pageable) {

        return commentRepo.findAll(pageable)
                .map(page-> modelMapper.map(page,CommentDto.class));
    }

    @Override
    public String deleteCommentById(Integer id) {
        commentRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","id",id.toString()) );

        commentRepo.deleteById(id);
        return "All data associated with "+id+" deleted Successfully!!";
    }

    @Override
    public String deleteAllCommentsByPost(String title) {
        List<Comment> comments=postRepo.findById(title)
                .orElseThrow(()-> new ResourceNotFoundException("Post","title",title)).getComments();

        try {
            commentRepo.deleteAll(comments);
            return "All data associated with Post title "+title+" deleted Successfully!!";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e.getCause());
        }
    }

    @Override
    public List<CommentDto> searchComment(String keyword,Pageable pageable) {

        try {
            return commentRepo.findByCommentContaining(keyword,pageable).stream()
                    .map(comment -> modelMapper.map(comment,CommentDto.class)).toList();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e.getCause());
        }
    }

    @Override
    public List<CommentDto> searchByUser(String keyword) {
        try {
            return commentRepo.findByUser_UserNameContaining(keyword).stream()
                    .map(comment -> modelMapper.map(comment,CommentDto.class)).toList();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e.getCause());
        }
    }

    @Override
    public List<CommentDto> searchByPost(String keyword) {

        try {
            return commentRepo.findByPost_TitleContaining(keyword).stream()
                    .map(comment -> modelMapper.map(comment,CommentDto.class)).toList();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e.getCause());
        }
    }
}
