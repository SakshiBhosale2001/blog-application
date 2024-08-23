package com.blog.playload;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
            ,message = "Invalid email address")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$"
            ,message = "Password must be between 8 and 20 characters long," +
            "contain at least one digit, one uppercase letter," +
            " one lowercase letter, and one special character. No whitespace allowed.")
    private String password;


    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,19}$"
            ,message = "Username must start with a letter and be 3 to 20 characters long." +
            "It can only contain letters, digits, underscores (_), or hyphens (-). No spaces or special characters are allowed ")
    private String userName;

    private String other_details;

    private List<RoleDto> roleDto;

    private List<PostDto> posts=new ArrayList<>();

    private List<CommentDto> commentDtos;
}
