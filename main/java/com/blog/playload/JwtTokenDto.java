package com.blog.playload;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenDto {

    private  String email;
    private String token;
}
