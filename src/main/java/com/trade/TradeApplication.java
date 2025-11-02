package com.trade;

import com.trade.entity.Stock;
import com.trade.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeApplication.class, args);
	}

	@Bean
	CommandLineRunner loadDummyStocks(StockRepository stockRepo) {
		return args -> {
			if (stockRepo.count() == 0) {
				stockRepo.save(new Stock(null, "AAPL", "Apple Inc.", 180.50));
				stockRepo.save(new Stock(null, "GOOG", "Alphabet Inc.", 2800.00));
				stockRepo.save(new Stock(null, "TSLA", "Tesla Inc.", 700.00));
				stockRepo.save(new Stock(null, "AMZN", "Amazon Inc.", 3300.00));
				stockRepo.save(new Stock(null, "MSFT", "Microsoft Corp.", 295.00));
			}
		};
	}
}
