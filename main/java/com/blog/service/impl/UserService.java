package com.blog.service.impl;

import com.blog.entity.Role;
import com.blog.entity.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.playload.LoginDto;
import com.blog.playload.UserDto;
import com.blog.repository.RoleRepo;
import com.blog.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements com.blog.service.UserService , UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=this.userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with"+ username +"not found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword()
                ,user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).toList());
    }

    @Override
    public ResponseEntity<UserDto> createNewUser(UserDto userDto) {


        userDto.setPassword(passwordEncoder().encode(userDto.getPassword()));


        User user=(this.modelMapper.map(userDto,User.class));
        user.getRoles().add(roleRepo.findById(1L).get());
        return new ResponseEntity<>
                (this.modelMapper.map(userRepo.save(user),UserDto.class),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {


        userDto.setPassword(passwordEncoder().encode(userDto.getPassword()));
        List<Role> roles = userDto.getRoleDto().stream()
                .map(roleDto -> roleRepo.findById(roleDto.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleDto.getId())))
                .toList();

        User user=(this.modelMapper.map(userDto,User.class));
        user.setRoles(roles);
        return new ResponseEntity<>
                (this.modelMapper.map(userRepo.save(user),UserDto.class),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UserDto userDto, String email) {

        User user=this.userRepo.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User","Email",email));

        user.setUserName(userDto.getUserName());
        user.setOther_details(userDto.getOther_details());

        return ResponseEntity.ok(this.modelMapper
                .map(userRepo.save(user)
                        ,UserDto.class));
    }


    @Override
    public Optional<UserDto> getUserByEmail(String email) {

        return Optional.of(this.modelMapper.map(userRepo.findByEmail(email)
                .orElseThrow( ()-> new ResourceNotFoundException("User","Email",email))
                ,UserDto.class));
    }

    @Override
    public List<UserDto> getAllUser()
    {
        return this.userRepo.findAll().stream()
                .map(user -> this.modelMapper.map(user,UserDto.class)).toList();
    }

    @Override
    public String deleteUserById(String email)
    {
            User user=this.userRepo.findByEmail(email)
                    .orElseThrow(()-> new ResourceNotFoundException("User","Email",email));

            this.userRepo.delete(user);
            return "Deleted Successfully";
    }

    @Override
    public String deleteAll() {
        try {
            this.userRepo.deleteAll();
            return "Deleted Successfully";
        }
        catch (Exception exception)
        {
            return "Unsuccessfully Attempt";
        }
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        return this.userRepo.findByUserNameContaining(keyword).stream()
                .map(user->modelMapper.map(user,UserDto.class)).toList();

    }


    public ResponseEntity<String> resetPassword(LoginDto request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User with email :"+request.getEmail()+"Not Found"));

        user.setPassword(passwordEncoder().encode(request.getPassword()));

        try {
            userRepo.save(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while resetting the password: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Password Modified Successfully");
    }

}
