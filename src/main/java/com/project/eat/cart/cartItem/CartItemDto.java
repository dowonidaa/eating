package com.project.eat.cart.cartItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDto {

    private Long cartItemId;
    private String cartItemName;
    private String cartItemOptionContent;
    private String cartItemUrl;
    private int quantity;
    private int price;
}
