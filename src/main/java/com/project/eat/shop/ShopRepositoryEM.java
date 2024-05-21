package com.project.eat.shop;

import com.project.eat.item.QItem;
import com.project.eat.item.itemOption.QItemOption;
import com.project.eat.shop.ShopVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.eat.item.QItem.*;
import static com.project.eat.item.itemOption.QItemOption.*;
import static com.project.eat.shop.QShopVO.*;

@Repository
public class ShopRepositoryEM {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ShopRepositoryEM(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public ShopVO findShopFetchJoinItem(Long shopId) {
        return queryFactory
                .select(shopVO)
                .from(shopVO)
                .join(shopVO.items, item).fetchJoin()
                .where(shopVO.shopId.eq(shopId))
                .fetchOne();

    }

    public ShopVO findShop(Long shopId) {
        return em.find(ShopVO.class, shopId);
    }

    public List<ShopVO> findAll() {
        return em.createQuery("select s from ShopVO s", ShopVO.class).getResultList();
    }
}
