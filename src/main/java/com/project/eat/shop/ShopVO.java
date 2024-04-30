package com.project.eat.shop;

import com.project.eat.cart.Cart;
import com.project.eat.item.Item;
import com.project.eat.member.Coupon;
import com.project.eat.order.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name="shop",uniqueConstraints = {
        @UniqueConstraint(
                columnNames= {"shop_id"}
        )
})
public class ShopVO {
    @Id  //pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increament
    @Column(name="shop_id")//컬럼이름 설정
    private Long shopId;

    @Column(name="shop_name",nullable = false)
    private String shopName;

    @Column(name="star_avg")
    private String starAvg;

    @Column(name="review_count")
    private int reviewCount;

    @Column(name="delivery_time")
    private String deliveryTime;

    @Column(name="delivery_price")
    private Integer deliveryPrice;

    @Column(name="run_time")
    private String runTime;

    @Column(name="shop_tel")
    private String shopTel;

    @Column(name="shop_addr")
    private String shopAddr;

    @Column(name="min_price")
    private String minPrice;

    @Column(name="tag")
    private String tag;

    @Column(name="cate_id",nullable = false)
    private int cateId;

    @Column(name="shop_thum")
    private String shopThum;

    @Column(name="min_price_int")
    private int minPriceInt;


    @OneToMany(mappedBy = "shop")
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Order> orders = new ArrayList<>();




}

