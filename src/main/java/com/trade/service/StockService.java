package com.trade.service;

import com.trade.entity.Stock;
import com.trade.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final Random random  = new Random();

    public List<Stock> getAllStocks(){
        return stockRepository.findAll();
    }

    public Stock getStockBySymbol(String symbol){
        return stockRepository.findBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Stock not found with symbol" + symbol));
    }
    @Scheduled(fixedRate = 5000)
    public void simulatePriceChanges(){
        List<Stock> stocks = stockRepository.findAll();
        for(Stock stock : stocks) {
            double change = (random.nextDouble() - 0.5) * 5;
            stock.setPrice(Math.max(1, stock.getPrice() + change));
        }
        stockRepository.saveAll(stocks);
    }
}

