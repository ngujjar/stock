package com.trade.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockListService {

    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${alphavantage.api.key}")
    private String apikey;

 public List<Map<String, String>> getAllStockFromAlphaVantage(){
     try{
         String url = BASE_URL + "?function=LISTING_STATUS&apikey=" + apikey;
         String csvData = restTemplate.getForObject(url, String.class);

         List<Map<String, String>> stocks = new ArrayList<>();
         BufferedReader reader = new BufferedReader(new StringReader(csvData));

         String line = reader.readLine();
         while ((line = reader.readLine()) != null){
             String[] fields = line.split(",");
             if(fields.length >= 3){
                 stocks.add(Map.of(
                         "symbol", fields[0],
                         "name", fields[1],
                         "exchange", fields[2]
                 ));
             }
         }
         return stocks;
     }catch (Exception e){
         throw new RuntimeException("failed to fetch stock list: " + e.getMessage());
     }
 }

}
