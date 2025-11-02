package com.trade.service;

import com.trade.entity.User;
import com.trade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Double getBalance(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBalance();
    }

    public User depositFunds(String email, Double amount){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
        user.setBalance(user.getBalance() + amount);
        return userRepository.save(user);
    }
}
