package com.project.eat.cart;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    EntityManager em;
    @Autowired
    private CartRepository cartRepository;

    @Test
    void findCart() {
        cartService.deleteCart("dowon456");
        em.flush();
        em.clear();


        cartService.createCart("dowon456");



        Cart dowon456 = cartRepository.findByMemberId("dowon456");
        CartDto findCart = cartService.findCartByMemberId("dowon456");

        assertThat(dowon456).isNotNull();
    }

}