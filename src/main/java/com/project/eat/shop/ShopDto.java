package com.project.eat.shop;

import com.project.eat.item.Item;
import com.project.eat.item.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShopDto {

    private Long shopId;
    private String shopName;
    private String starAvg;
    private String minPrice;
    private String runTime;
    private String shopAddr;
    private String deliveryTime;
    private String shopTel;
    private int reviewCount;
    private List<ItemDto> items;
    private int deliveryPrice;
    private int minPriceInt;

}
