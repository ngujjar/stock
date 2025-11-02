package com.trade.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String stockSymbol;
    private Double price;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    private LocalDateTime orderTime;

    @ManyToOne
    @JoinColumn(name = "user_id")

    private User user;
}
