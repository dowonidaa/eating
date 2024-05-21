package com.project.eat.item.itemOption;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemOptionDto {

    private Long id;
    private String content;
    private int price;
}
