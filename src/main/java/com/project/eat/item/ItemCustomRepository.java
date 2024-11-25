package com.project.eat.item;

import java.util.List;

public interface ItemCustomRepository {

    Item findByItemFetchJoin(Long itemId);

    List<Item> findItemsExcludingItemId(Long shopId, Long itemId);

    List<Item> findByShopId(Long shopId);
}
