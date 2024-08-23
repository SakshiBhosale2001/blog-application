package com.blog.playload;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto
{
    private int id;

    @NotEmpty
    private String comment;

    private Date commentedDate;


    private PostDto postDto;


    private UserDto userDto;
}
