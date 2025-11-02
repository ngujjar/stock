package com.trade.repository;

import com.trade.entity.Order;
import com.trade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByuser(User user);
}
