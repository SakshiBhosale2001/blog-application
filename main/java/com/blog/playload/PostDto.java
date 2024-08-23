package com.blog.playload;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @Pattern(regexp = "^[a-zA-Z]+([a-z0-9A-Z @#$%^&+=! ]*)$")
    private String title;

    private String content;

    private String image;

    private Date addedDate;


    private UserDto userDto;

    private CategoryDto categoryDto;

    private List<CommentDto> commentDtos;
}
