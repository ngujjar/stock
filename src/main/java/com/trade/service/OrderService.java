//package com.trade.service;
//
//import com.trade.entity.Holding;
//import com.trade.entity.Order;
//import com.trade.entity.OrderType;
//import com.trade.entity.User;
//import com.trade.repository.HoldingRepository;
//import com.trade.repository.OrderRepository;
//import com.trade.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//
//    private final OrderRepository orderRepository;
//    private final UserRepository userRepository;
//    private final HoldingRepository holdingRepository;
//
//    public Order placeOrder(String email, String symbol, Double price, Integer quantity, OrderType type){
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("user not found"));
//
//        double totalAmount = price*quantity;
//        if(type == OrderType.BUY){
//            if(user.getBalance()< totalAmount){
//                throw new RuntimeException("Insufficient balance to buy" + symbol);
//            }
//            user.setBalance(user.getBalance() - totalAmount);
//            userRepository.save(user);
//
//            Holding holding = holdingRepository.findByUserAndSymbol(user, symbol).orElse(null);
//            if(holding == null){
//                holding = Holding.builder()
//                        .user(user)
//                        .symbol(symbol)
//                        .quantity(quantity)
//                        .build();
//            }else{
//                holding.setQuantity(holding.getQuantity() + quantity);
//            }
//            holdingRepository.save(holding);
//        } else if (type == OrderType.SELL) {
//            Holding holding = holdingRepository.findByUserAndSymbol(user, symbol)
//                    .orElseThrow(() -> new RuntimeException("You dont own any shares of" + symbol ));
//
//            if (holding.getQuantity() < quantity){
//                throw new RuntimeException("Not enough share to sell. You have " + holding.getQuantity());
//            }
//            holding.setQuantity(holding.getQuantity() - quantity);
//            holdingRepository.save(holding);
//
//            user.setBalance(user.getBalance() + totalAmount);
//            userRepository.save(user);
//
//        }
//        Order order = Order.builder()
//                .user(user)
//                .stockSymbol(symbol)
//                .price(price)
//                .quantity(quantity)
//                .orderType(type)
//                .orderTime(LocalDateTime.now())
//                .build();
//
//
//       return orderRepository.save(order);
//
//    }
//    public List<Order> getOrderHistory(String email){
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return orderRepository.findByuser(user);
//    }
//}


package com.trade.service;

import com.trade.entity.Holding;
import com.trade.entity.Order;
import com.trade.entity.OrderType;
import com.trade.entity.User;
import com.trade.repository.HoldingRepository;
import com.trade.repository.OrderRepository;
import com.trade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;

    @Transactional
    public Order placeOrder(String email, String symbol, Double price, Integer quantity, OrderType type) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double totalAmount = price * quantity;

        // -------- BUY --------
        if (type == OrderType.BUY) {
            if (user.getBalance() < totalAmount) {
                throw new RuntimeException("Insufficient balance to buy " + symbol);
            }

            user.setBalance(user.getBalance() - totalAmount);
            userRepository.save(user);

            Holding holding = holdingRepository.findByUserAndSymbol(user, symbol).orElse(null);

            if (holding == null) {
                holding = Holding.builder()
                        .user(user)
                        .symbol(symbol)
                        .quantity(quantity)
                        .avgPrice(price)
                        .build();
            } else {
                int oldQty = holding.getQuantity();
                double oldAvg = holding.getAvgPrice();

                int newQty = oldQty + quantity;
                double newAvg = ((oldQty * oldAvg) + (quantity * price)) / newQty;

                holding.setQuantity(newQty);
                holding.setAvgPrice(newAvg);
            }

            holdingRepository.save(holding);
        }

        // -------- SELL --------
        else if (type == OrderType.SELL) {
            Holding holding = holdingRepository.findByUserAndSymbol(user, symbol)
                    .orElseThrow(() -> new RuntimeException("You don’t own any shares of " + symbol));

            if (holding.getQuantity() < quantity) {
                throw new RuntimeException("Not enough shares to sell. You have " + holding.getQuantity());
            }

            holding.setQuantity(holding.getQuantity() - quantity);

            if (holding.getQuantity() == 0) {
                holdingRepository.delete(holding);
            } else {
                holdingRepository.save(holding);
            }

            user.setBalance(user.getBalance() + totalAmount);
            userRepository.save(user);
        }

        // -------- RECORD ORDER --------
        Order order = Order.builder()
                .user(user)
                .stockSymbol(symbol)
                .price(price)
                .quantity(quantity)
                .orderType(type)
                .orderTime(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }

    public List<Order> getOrderHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByuser(user);
    }
}
