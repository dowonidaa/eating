package com.project.eat.order;

import com.project.eat.order.orderItem.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private int price;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private String PaymentMethod;
    private Long shopId;
    private String shopThum;
    private LocalDateTime orderDate;
    private String shopName;
    private String itemsName;
    private boolean reviewExists;
    private List<OrderItemDto> orderItems;
    private String orderTel;
    private String memberNotes;
    private String shopTel;
    private String orderAddress;
    private int totalPrice;
    private int orderPrice;
    private int discount;
    private String memberId;

    public OrderDto(Long id, int price, OrderType orderType, OrderStatus orderStatus, String paymentMethod, Long shopId, String shopThum, LocalDateTime orderDate, String shopName, String itemsName, boolean reviewExists) {
        this.id = id;
        this.price = price;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        PaymentMethod = paymentMethod;
        this.shopId = shopId;
        this.shopThum = shopThum;
        this.orderDate = orderDate;
        this.shopName = shopName;
        this.itemsName = itemsName;
        this.reviewExists = reviewExists;
    }







}

