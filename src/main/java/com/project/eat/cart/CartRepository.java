package com.project.eat.cart;

import com.project.eat.cart.cartItem.QCartItem;
import com.project.eat.shop.QShopVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.eat.cart.QCart.*;
import static com.project.eat.cart.cartItem.QCartItem.*;
import static com.project.eat.shop.QShopVO.*;

@Repository
public class CartRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CartRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Cart findCart(Long id) {
       return em.find(Cart.class, id);
    }

    public void save(Cart cart) {
        em.persist(cart);
    }

    public void delete(Cart findCart) {
        em.remove(findCart);
    }

    public void flush() {
        em.flush();
    }

    public Cart findByMemberId(String memberId) {
        return queryFactory
                .select(cart)
                .from(cart)
                .leftJoin(cart.shop, shopVO).fetchJoin()
                .leftJoin(cart.cartItems, cartItem).fetchJoin()
                .where(cart.member.id.eq(memberId))
                .fetchOne();
    }


}
