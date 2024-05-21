package com.project.eat.cart;

import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.cart.cartItem.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartDto {

    private Long cartId;
    private Long ShopId;
    private int minPriceInt;
    private int deliveryPrice;
    private List<CartItemDto> cartItems;
    private int totalPrice;

    public CartDto(Long cartId) {
        this.cartId = cartId;
    }
}
