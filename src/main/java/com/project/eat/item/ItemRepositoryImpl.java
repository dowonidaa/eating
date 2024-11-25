package com.project.eat.item;

import com.project.eat.item.itemOption.QItemOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.eat.item.QItem.item;
import static com.project.eat.shop.QShopVO.shopVO;

@Repository
public class ItemRepositoryImpl implements ItemCustomRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    public List<Item> findByShopId(Long shopId) {
       return queryFactory
               .selectFrom(item)
               .join(item.shop, shopVO).fetchJoin()
               .where(item.shop.shopId.eq(shopId))
               .fetch();
    }

    public Item findByItemFetchJoin(Long itemId) {
        return queryFactory
                .select(item)
                .from(item)
                .join(item.shop, shopVO).fetchJoin()
                .join(item.itemOptions, QItemOption.itemOption).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();
    }


    public List<Item> findItemsExcludingItemId(Long shopId, Long itemId) {

        return queryFactory
                .select(item)
                .from(item)
                .where(item.shop.shopId.eq(shopId), item.id.notIn(itemId))
                .fetch();
    }
}
