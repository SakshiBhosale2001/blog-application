package com.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "role")
public class Role {

    @Id
    private long id;

    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
