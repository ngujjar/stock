package com.trade.controller;

import com.trade.entity.Stock;
import com.trade.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<Stock> getAllStocks(){
        return stockService.getAllStocks();
    }

    @GetMapping("/{symbol}")
    public Stock getStockBySymbol(@PathVariable String symbol){
        return stockService.getStockBySymbol(symbol);
    }
}
