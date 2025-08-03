package com.example.SaaSApplication.user.serviceImpl;

import com.example.SaaSApplication.entity.TblUserDetails;
import com.example.SaaSApplication.user.dto.UserDto;
import com.example.SaaSApplication.user.repository.TblUserDetailsRepository;
import com.example.SaaSApplication.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    TblUserDetailsRepository userDetailsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserDto userDto) {
        System.out.println("inside service");
        String res = "";
        try{
            TblUserDetails user = new TblUserDetails();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRole("User");
            user.setActive(true);
            userDetailsRepository.save(user);
            res = "User Registration Successfull!";
        }catch (Exception e){
            e.printStackTrace();
            res = "error while registering user: "+e.getMessage();
        }
        return res;
    }

    @Override
    public String loginUser(String email, String password) {
        Optional<TblUserDetails> userOpt = userDetailsRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Invalid email or user not found.";
        }
        TblUserDetails user = userOpt.get();
        if (Boolean.TRUE.equals(user.getAccountLocked())) {
            long lockedDuration = new Date().getTime() - user.getLockedAt().getTime();
            if (lockedDuration >= 1 * 60 * 1000) { // 10 minutes
                user.setAccountLocked(false);
                user.setFailedAttempts(0);
                user.setLockedAt(null);
                userDetailsRepository.save(user);
            } else {
                return "Account is locked. Try again after 1 minutes.";
            }
        }

        // ✅ Password check
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setFailedAttempts(0);
            userDetailsRepository.save(user);
            return "Login successful for: " + user.getName();
        } else {
            int attempts = user.getFailedAttempts() == null ? 1 : user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 3) {
                user.setAccountLocked(true);
                user.setLockedAt(new Date());  // set current time
                userDetailsRepository.save(user);
                return "Account locked due to 3 failed attempts. Try again after 10 minutes.";
            } else {
                userDetailsRepository.save(user);
                return "Invalid password. Attempt " + attempts + " of 3.";
            }
        }
    }
}
