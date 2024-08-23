package com.blog.repository;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>
{
    Optional<User> findByEmail(String userName);
    Optional<User> findByUserName(String userName);
    List<User> findByUserNameContaining(String keyword);
    /*List<Post> findByCategory_CategoryNameContaining(String keyword);
    List<Post> findByUser_UserNameContaining(String keyword);*/
}
