package com.trade.controller;

import com.trade.dto.AuthRequestDTO;
import com.trade.dto.AuthResponseDTO;
import com.trade.dto.RegisterRequestDTO;
import com.trade.entity.User;
import com.trade.repository.UserRepository;
import com.trade.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

//    @PostMapping("/register")
//    public String register(@RequestBody User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setBalance(10000.0);
//        userRepository.save(user);
//        return "User registered successfully";
//    }
//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody User user) {
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//                String token = jwtUtil.generateToken(user.getEmail());
//                return Map.of("token", token);
//
//    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDTO request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(10000.0);

        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponseDTO(token);
    }
}
