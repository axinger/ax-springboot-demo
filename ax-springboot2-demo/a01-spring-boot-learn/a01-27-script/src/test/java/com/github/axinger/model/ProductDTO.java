package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    
    public String productId;

    public Integer number;
    private Integer productPrice;
    private Integer  totalPrice;
    
    private Double a;
    private Double b;
    private BigDecimal c;
    
    
//    public void add() {
//       totalPrice = productPrice*number;
//       
//       c = a
//    }
}
