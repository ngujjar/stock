package com.trade.controller;

import com.trade.service.StockListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockListController {
    private final StockListService stockListService;

    @GetMapping("/all")
    public List<Map<String, String>> getAllStocks(){
        return stockListService.getAllStockFromAlphaVantage();
    }

}
