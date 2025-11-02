package com.trade.controller;

import com.trade.dto.OrderRequest;
import com.trade.entity.Holding;
import com.trade.entity.User;
import com.trade.repository.HoldingRepository;
import com.trade.repository.UserRepository;
import com.trade.service.StockPriceService;
import com.trade.entity.Order;
import com.trade.entity.OrderType;
import com.trade.repository.OrderRepository;
import com.trade.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StockPriceService stockPriceService;
    private final HoldingRepository holdingRepository;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<?> getStockPrice(@PathVariable String symbol) {
        try {
            double price = stockPriceService.getLivePrice(symbol);
            if (price <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Could not fetch price for symbol: " + symbol
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "symbol", symbol.toUpperCase(),
                    "price", price
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to fetch stock price: " + e.getMessage()
            ));
        }
    }

//@PostMapping("/buy")
//public ResponseEntity<String> buyStock(@RequestBody @Valid OrderRequest request, Authentication auth) {
//    String username = auth.getName();
//    double price = stockPriceService.getLivePrice(request.getSymbol());
//    double totalCost = price * request.getQuantity();
//
//    if (price <= 0) {
//        return ResponseEntity.badRequest().body("Could not fetch current price for " + request.getSymbol());
//    }
//
//    User user = userRepository.findByEmail(username)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    if (user.getBalance() < totalCost) {
//        return ResponseEntity.badRequest().body("Insufficient balance. You need $" + totalCost + " to buy this stock.");
//    }
//
//    user.setBalance(user.getBalance() - totalCost);
//    userRepository.save(user);
//
//    Order order = Order.builder()
//            .stockSymbol(request.getSymbol())
//            .price(price)
//            .quantity(request.getQuantity())
//            .orderType(OrderType.BUY)
//            .user(user)
//            .build();
//
//    orderRepository.save(order);
//
//    return ResponseEntity.ok("Bought " + request.getQuantity() + " shares of " + request.getSymbol() + " at $" + price);
//}

    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(@RequestBody @Valid OrderRequest request, Authentication auth) {
        String username = auth.getName();
        double price = stockPriceService.getLivePrice(request.getSymbol());
        int qty = request.getQuantity();
        double totalCost = price * qty;

        if (price <= 0) {
            return ResponseEntity.badRequest().body("Could not fetch current price for " + request.getSymbol());
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() == null || user.getBalance() < totalCost) {
            return ResponseEntity.badRequest().body("Insufficient balance. You need $" + totalCost + " to buy this stock.");
        }

        Order savedOrder = orderService.placeOrder(username, request.getSymbol(), price, qty, OrderType.BUY);

        return ResponseEntity.ok("Bought " + request.getQuantity() + " shares of " + request.getSymbol() + " at $" + price);
    }

//    @PostMapping("/sell")
//public ResponseEntity<String> sellStock(@RequestBody Map<String, Object> payload, Authentication auth) {
//    String email = auth.getName();
//    String symbol = (String) payload.get("symbol");
//    double price = Double.parseDouble(payload.get("price").toString());
//    int qty = Integer.parseInt(payload.get("quantity").toString());
//
//    User user = userRepository.findByEmail(email)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    double totalGain = price * qty;
//
//    user.setBalance(user.getBalance() + totalGain);
//    userRepository.save(user);
//
//    Order order = Order.builder()
//            .stockSymbol(symbol)
//            .price(price)
//            .quantity(qty)
//            .orderType(OrderType.SELL)
//            .user(user)
//            .build();
//
//    orderRepository.save(order);
//
//    return ResponseEntity.ok("Sold " + qty + " shares of " + symbol + " for $" + totalGain);
//}

    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(@RequestBody OrderRequest request, Authentication auth) {
        String email = auth.getName();
        String symbol = request.getSymbol().toUpperCase();

        double livePrice = stockPriceService.getLivePrice(symbol);
        if (livePrice <= 0) {
            return ResponseEntity.badRequest().body("Unable to fetch live price for " + symbol);
        }

        Order order = orderService.placeOrder(email, symbol, livePrice, request.getQuantity(), OrderType.SELL);

        return ResponseEntity.ok(
                "✅ Sold " + request.getQuantity() + " shares of " + symbol + " at $" + livePrice
        );
    }
//    @GetMapping("/holdings")
//    public ResponseEntity<?> getHoldings(Authentication auth) {
//        String email = auth.getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<Holding> holdings = holdingRepository.findAll()
//                .stream()
//                .filter(h -> h.getUser().equals(user))
//                .toList();
//
//        return ResponseEntity.ok(holdings);
//    }
@GetMapping("/holdings")
public ResponseEntity<?> getHoldings(Authentication auth) {
    String email = auth.getName();
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Holding> holdings = holdingRepository.findByUser(user);
    return ResponseEntity.ok(holdings);
}


    @GetMapping("/history")
    public List<Order> getHistory(Authentication auth){
        String email = auth.getName();
        return orderService.getOrderHistory(email);
    }
}
