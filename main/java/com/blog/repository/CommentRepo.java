package com.blog.repository;

import com.blog.entity.Comment;
import com.blog.entity.User;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {
    List<Comment> findByUser_UserNameContaining(String keyword);
    Page<Comment> findByCommentContaining(String keyword, Pageable pageable);
    Optional<List<Comment>> findByUser_UserName(String userName);
    List<Comment> findByUser(User user);
    List<Comment> findByPost_TitleContaining(String keyword);

}
