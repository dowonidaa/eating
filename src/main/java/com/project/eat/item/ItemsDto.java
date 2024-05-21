package com.project.eat.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsDto {

    private Long itemId;
    private String itemName;
    private int itemPrice;
}
