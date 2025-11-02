package com.trade.controller;

import com.trade.entity.Holding;
import com.trade.entity.User;
import com.trade.repository.UserRepository;
import com.trade.service.PortfolioService;
import com.trade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PortfolioService portfolioService;

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(userService.getBalance(email));
    }

    @PostMapping("/deposit")
    public ResponseEntity<User> depositFunds(Authentication auth, @RequestParam Double amount){
        String email = auth.getName();
        return ResponseEntity.ok(userService.depositFunds(email, amount));
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<Holding>> getPortfolio(Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(portfolioService.gerUserPortfolio(email));
    }
}
