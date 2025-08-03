package com.example.SaaSApplication.user.controller;

import com.example.SaaSApplication.user.dto.UserDto;
import com.example.SaaSApplication.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String getUsers(){
        return "hello users";
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> regidterUser(@RequestBody UserDto user) {

        System.out.println(user.getEmail()+",  "+user.getName()+",  "+user.getPassword());
        String response = userService.registerUser(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        String response = userService.loginUser(userDto.getEmail(), userDto.getPassword());
        return ResponseEntity.ok(response);
    }
}
