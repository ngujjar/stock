package com.trade.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Stock symbol is required")
    private String symbol;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

}
