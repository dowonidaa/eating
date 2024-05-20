package com.project.eat.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.project.eat.item.QItem.item;
import static com.project.eat.member.QCoupon.coupon;
import static com.project.eat.member.QMemberVO_JPA.memberVO_JPA;
import static com.project.eat.order.QOrder.order;
import static com.project.eat.order.orderItem.QOrderItem.orderItem;
import static com.project.eat.review.QReviewVO.reviewVO;
import static com.project.eat.shop.QShopVO.shopVO;
@Slf4j
@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public OrderRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }


    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public void deleteOne(Order one) {
        em.remove(one);
    }

    public List<Order> search(String memberId, SearchForm form) {

        return queryFactory
                .selectFrom(order)
                .join(order.member, memberVO_JPA).fetchJoin()
                .join(order.shop, shopVO).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .leftJoin(order.review, reviewVO).fetchJoin()
                .leftJoin(order.coupon, coupon).fetchJoin()
                .where(
                        memberIdEq(memberId),
                        shopNameOrItemNameContains(form.getSearchText()),
                        orderTypeEq(form.getSelectedType()),
                        dateBetween(form))
                .orderBy(order.orderDate.desc())
                .offset(form.getOffset())
                .limit(form.getPageBlock())
                .fetch();
    }






    public List<Order> findAllPage(String memberId, int pageBlock) {
        return queryFactory
                .selectFrom(order)
                .join(order.member, memberVO_JPA).fetchJoin()
                .join(order.shop, shopVO).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .leftJoin(order.review, reviewVO).fetchJoin()
                .leftJoin(order.coupon, coupon).fetchJoin()
                .where(memberIdEq(memberId))
                .orderBy(order.orderDate.desc())
                .limit(pageBlock)
                .fetch();
    }


    public Long pageCount(String memberId) {
        return
//                em.createQuery("select count(o) from Order o where o.member.id = :memberId", Long.class).setParameter("memberId", memberId).getSingleResult();
        queryFactory.select(order.count()).from(order).where(order.member.id.eq(memberId)).fetchOne();
    }

    public Long searchTotalCount(String memberId, SearchForm form) {
        return queryFactory
                .select(order.countDistinct())
                .from(order)
                .join(order.member, memberVO_JPA)
                .join(order.shop, shopVO)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item)
                .leftJoin(order.review, reviewVO)
                .leftJoin(order.coupon, coupon)
                .where(
                        memberIdEq(memberId),
                        shopNameOrItemNameContains(form.getSearchText()),
                        orderTypeEq(form.getSelectedType()),
                        dateBetween(form))
                .orderBy(order.orderDate.desc())
                .fetchOne();

    }

    public List<Order> searchListBetweenDates(String memberId, SearchForm form) {
        String jpql = "select distinct o from Order o join o.shop s join o.orderItems oi join oi.item i where o.member.id = :memberId and (lower(s.shopName) like lower(:searchText) or lower(i.itemName) like lower(:searchText))";

        if (!form.getSelectedType().equals("all")) {
            jpql += " and o.orderType = :orderType";
        }

        if (form.getSearchOption().equals("dateRange")) {
            jpql += "  and cast(o.orderDate as date) between :startDate and :endDate";
        }

        jpql += " order by o.orderDate desc";

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setParameter("memberId", memberId)
                .setParameter("searchText", "%"+form.getSearchText()+"%");

        if (!form.getSelectedType().equals("all")) {
            query.setParameter("orderType", OrderType.valueOf(form.getSelectedType().toUpperCase()));
        }
        if(form.getSearchOption().equals("dateRange")) {
            query.setParameter("startDate", form.getStartDate())
                    .setParameter("endDate", form.getEndDate());
        }

        query.setFirstResult(form.getOffset());
        query.setMaxResults(form.getPageBlock());
        return query.getResultList();
    }

    public Long searchPageCount(String memberId, SearchForm form) {



        String jpql = "select count(distinct o) from Order o join o.shop s join o.orderItems oi join oi.item i where o.member.id = :memberId and (lower(s.shopName) like lower(:searchText) or lower(i.itemName) like lower(:searchText))";



        if (!form.getSelectedType().equals("all")) {
            jpql += " and o.orderType = :orderType";
        }

        if (form.getSearchOption().equals("dateRange")) {
            jpql += "  and cast(o.orderDate as date) between :startDate and :endDate";
        }


        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("memberId", memberId)
                .setParameter("searchText", "%"+form.getSearchText()+"%");

        if (!form.getSelectedType().equals("all")) {
            query.setParameter("orderType", OrderType.valueOf(form.getSelectedType().toUpperCase()));
        }
        if(form.getSearchOption().equals("dateRange")) {
            query.setParameter("startDate", form.getStartDate())
                    .setParameter("endDate", form.getEndDate());
        }

        return query.getSingleResult();

    }

    private BooleanExpression dateBetween(SearchForm form) {
        if (form.getSearchOption().equals("dateRange")) {
            return Expressions.dateTemplate(LocalDate.class, "cast({0} as date)", order.orderDate).between(form.getStartDate(), form.getEndDate());
        }
        return null;
    }

    private BooleanExpression orderTypeEq(String orderType) {
        if (orderType.equals("all")){
            return null;
        }
        OrderType enumOrderType= OrderType.valueOf(orderType.toUpperCase());
        return order.orderType.eq(enumOrderType);
    }

    private BooleanExpression shopNameOrItemNameContains(String searchText) {
        if(StringUtils.hasText(searchText)) {
            return shopVO.shopName.lower().contains(searchText.toLowerCase()).or(item.itemName.lower().contains(searchText.toLowerCase()));
        }
        return null;
    }

    private BooleanExpression memberIdEq(String memberId) {
        if(StringUtils.hasText(memberId)) {
            log.info("memberid = {}", memberId);
            return order.member.id.eq(memberId);
        }
        return null;
    }
}
