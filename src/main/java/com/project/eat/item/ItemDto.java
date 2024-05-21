package com.project.eat.item;

import com.project.eat.item.itemOption.ItemOption;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {

    private Long itemId;
    private String itemName;
    private Long shopId;
    private String itemUrl;
    private String itemDescription;
    private int itemPrice;
    private String minPrice;
    private List<ItemOption> itemOptions;

}
