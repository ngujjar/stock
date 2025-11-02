package com.trade.service;

import com.trade.entity.Holding;
import com.trade.entity.User;
import com.trade.repository.HoldingRepository;
import com.trade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;

    public List<Holding> gerUserPortfolio(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException ("User not found"));
        return holdingRepository.findByUser(user);
    }
}
