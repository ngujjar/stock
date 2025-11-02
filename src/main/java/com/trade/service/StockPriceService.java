//package com.trade.service;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class StockPriceService {
//
//    @Value("${alphavantage.api.key}")
//    private String apiKey;
//
//    private static final String BASE_URL = "https://www.alphavantage.co/query";
//
//    public Double getLivePrice(String symbol){
//        try {
//            String url = BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "$apikey=" + apiKey;
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//            JSONObject json = new JSONObject(response.getBody());
//            JSONObject globalQuote = json.getJSONObject("Global Quote");
//
//            String priceStr = globalQuote.getString("05. price");
//            return Double.parseDouble(priceStr);
//
//        }catch (Exception e){
//            throw new RuntimeException("Failed to fetch stock price: " + e.getMessage());
//        }
//    }
//}

package com.trade.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://www.alphavantage.co/query";

    public Double getLivePrice(String symbol) {
        try {
            String url = BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JSONObject json = new JSONObject(response.getBody());

            if (!json.has("Global Quote") || json.getJSONObject("Global Quote").isEmpty()) {
                throw new RuntimeException("No price data found for symbol: " + symbol);
            }

            JSONObject globalQuote = json.getJSONObject("Global Quote");

            String priceStr = globalQuote.optString("05. price", "0.0");
            return Double.parseDouble(priceStr);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock price for " + symbol + ": " + e.getMessage());
        }
    }
}
