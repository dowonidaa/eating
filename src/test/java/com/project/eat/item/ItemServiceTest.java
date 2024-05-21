package com.project.eat.item;

import com.project.eat.item.itemOption.ItemOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    void findItem(){
        //given
        ItemDto one = itemService.findByItemFetchJoin(248L);

        System.out.println("one = " + one);
        List<ItemOption> itemOptions = one.getItemOptions().stream().sorted(Comparator.comparing(ItemOption::getPrice)).toList();

        for (ItemOption itemOption : itemOptions) {
            System.out.println("itemOption = " + itemOption.getId());
        }



        //when

        //then
     }

     @Test
     void findByShopId() {
         List<Item> items = itemService.findByShopId(1L).stream().filter(item -> !item.getId().equals(248L)).toList();

         for (Item item : items) {
             System.out.println("item = " + item.getItemName());
         }
     }



}