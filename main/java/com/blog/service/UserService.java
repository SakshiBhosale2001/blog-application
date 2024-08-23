package com.blog.service;
import com.blog.playload.UserDto;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;


public interface UserService {

    ResponseEntity<UserDto> createNewUser(UserDto userDto);

    ResponseEntity<UserDto> createUser(UserDto userDto);
    ResponseEntity<UserDto> updateUser(UserDto userDto, String email);
    Optional<UserDto> getUserByEmail(String email);
    List<UserDto> getAllUser();
    String deleteUserById(String email);
    String deleteAll();
    List<UserDto> searchUser(String keyword);

}
