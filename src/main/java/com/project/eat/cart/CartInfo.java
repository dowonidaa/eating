package com.project.eat.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartInfo {

    private int cartCount;
    private Long shopId;
}
