package com.example.SaaSApplication.user.service;

import com.example.SaaSApplication.user.dto.UserDto;

public interface UserService {

    String registerUser(UserDto userDto);

    String loginUser(String email, String password);

}
