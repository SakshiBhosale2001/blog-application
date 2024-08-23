package com.blog.controller;

import com.blog.configuration.security.JwtToken;
import com.blog.playload.*;
import com.blog.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtToken jwtToken;


    @PostMapping("/newUser")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        return this.userService.createNewUser(userDto);
    }


    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody UserDto userDto)
    {
        return this.userService.createUser(userDto);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginDto loginDto) {

        //verifying Authentication
        Authentication authentication=manager.authenticate(new UsernamePasswordAuthenticationToken
                (loginDto.getEmail(), loginDto.getPassword()));

        //getting user details
        UserDetails userDetails = userService.loadUserByUsername(loginDto.getEmail());

        //generating tokens
        String token = jwtToken.generateToken(userDetails);

        //retrieving token & email
        JwtTokenDto jwtTokenDto = JwtTokenDto.builder().token(token).email(userDetails.getUsername())
                .build();

        return new ResponseEntity<>(jwtTokenDto, HttpStatus.OK);
    }



    @PutMapping("/reset/password")
    public ResponseEntity<String> checkUser(@RequestBody LoginDto request)
    {
        return userService.getUserByEmail(request.getEmail()).isPresent() ? userService.resetPassword(request)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Can Not Proceed a Request...Please Enter a Registered Email...! \n " +
                        "OR \n Kindly Register First ! ...Thank You!! ");

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update/{email}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable("email") String email)
    {
        return this.userService.updateUser(userDto,email);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/userDetails/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable("email") String email)
    {
        return ResponseEntity.ok(this.userService.getUserByEmail(email).get());
    }

    @PreAuthorize("hasRole('ROLE_'ADMIN)")
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAllUser()
    {
        return ResponseEntity.ok(this.userService.getAllUser());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("email") String email)
    {

        return ResponseEntity.ok(new ApiResponse(this.userService.deleteUserById(email),true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/allUsers")
    public ResponseEntity<String> deleteAllUser()
    {
        return ResponseEntity.ok(this.userService.deleteAll());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keyword") String keyword)
    {
        return ResponseEntity.ok(this.userService.searchUser(keyword));
    }

}
