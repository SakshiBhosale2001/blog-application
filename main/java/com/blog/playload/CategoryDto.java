package com.blog.playload;

import com.blog.entity.Post;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {

    @NotEmpty(message = "categoryName must not be Null or Empty")
    @Pattern(regexp = "^[a-zA-Z]+([ _][a-zA-Z]+)*$", message = "categoryName must be between 2 to 19 characters long. Only uppercase, lowercase letters and underscore are allowed.")
    private String categoryName;

    private String categoryDescription;

    private List<PostDto> posts=new ArrayList<>();
}
