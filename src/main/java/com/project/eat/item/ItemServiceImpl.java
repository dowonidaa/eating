package com.project.eat.item;

import com.project.eat.shop.ShopRepositoryEM;
import com.project.eat.shop.ShopVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl {

    private final ItemRepository itemRepository;
    private final ShopRepositoryEM shopRepository;

    @Transactional
    public void creatItem(ItemForm form) {
        ShopVO shop = shopRepository.findShop(form.getShopId());
        Item item = Item.create(form, shop);
        itemRepository.save(item);
    }
    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow();
    }

    public ItemDto findByItemFetchJoin(Long itemId) {
        Item findItem = itemRepository.findByItemFetchJoin(itemId);
        return ItemDto.of(findItem);
    }

    @Transactional
    public void updateItem(Long itemId, ItemForm form) {
        Item findItem = itemRepository.findById(itemId).orElseThrow();
        findItem.updateItemInfo(form);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }


    public List<Item> findByShopId(Long shopId) {
        return itemRepository.findByShopId(shopId);
    }

    public List<ItemsDto> findItemsExcludingItemId(Long shopId, Long itemId) {
        List<Item> findItems = itemRepository.findItemsExcludingItemId(shopId, itemId);
        return findItems.stream().map(i -> new ItemsDto(i.getId(), i.getItemName(), i.getItemPrice())).toList();
    }
}
