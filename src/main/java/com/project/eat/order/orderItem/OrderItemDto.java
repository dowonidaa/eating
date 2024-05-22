package com.project.eat.order.orderItem;

import com.project.eat.order.orderItemOption.OrderItemOptionDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderItemDto {
    private int itemPrice;
    private String itemName;
    private List<OrderItemOptionDto> orderItemOptions;
    private int orderItemPrice;
    private int quantity;
}
