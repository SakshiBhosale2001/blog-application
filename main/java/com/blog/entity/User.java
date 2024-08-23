package com.blog.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
            ,message = "Invalid email address")
    @Column(name = "email_address",unique = true,nullable = false)
    private String email;

    @NotEmpty(message = "password must not be null")
    private String password;

    @Column(name = "user_name",nullable = false,unique = true)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,19}$"
            ,message = "Username must start with a letter and be 3 to 20 characters long." +
            "It can only contain letters, digits, underscores (_), or hyphens (-). No spaces or special characters are allowed ")
    private String userName;

    private String other_details;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "role")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Post> post=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private  List<Comment> comments;
}
