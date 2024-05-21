package com.project.eat.cart;

import com.project.eat.cart.cartItem.CartItemDto;
import com.project.eat.cart.cartOption.CartItemOption;
import com.project.eat.member.MemberRepositoryEM;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.shop.ShopRepositoryEM;
import com.project.eat.shop.ShopVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepositoryEM memberRepository;
    private final ShopRepositoryEM shopRepository;

    public Cart findCart(Long cartId) {
        return cartRepository.findCart(cartId);
    }


    @Transactional
    public void createCart(String memberId) {
        Cart cart = new Cart();
        MemberVO_JPA member = memberRepository.findOne(memberId);
        cart.setMember(member);
        cartRepository.save(cart);
        member.addCart(cart);
    }

    @Transactional
    public int getTotalPrice(String memberId) {
        MemberVO_JPA findMember = memberRepository.findOne(memberId);
        findMember.getCart().totalPrice();


        return findMember.getCart().getTotalPrice();
    }

    @Transactional
    public void deleteAndCreateCart(String memberId, Long shopId) {
        MemberVO_JPA findMember = memberRepository.findOne(memberId);
        Cart findCart = findMember.getCart();
        cartRepository.delete(findCart);

        cartRepository.flush();
        log.info("context flush");
        Cart cart = new Cart();
        MemberVO_JPA member = memberRepository.findOne(memberId);
        ShopVO shop = shopRepository.findShop(shopId);

        cart.setMember(member);
        cart.setShop(shop);

        cartRepository.save(cart);
        member.addCart(cart);

    }

    @Transactional
    public void deleteCart(String memberId) {
        MemberVO_JPA findMember = memberRepository.findOne(memberId);
        Cart cart = findMember.getCart();
        cartRepository.delete(cart);

    }

    public CartInfo cartInfo(String memberId) {
        int cartCount = 0;
        Long shopId = null;
        if (memberId != null) {
            Cart findCart = cartRepository.findByMemberId(memberId);
            if (findCart != null && findCart.getShop() != null) {
                cartCount = findCart.getCartItems().size();
                shopId = findCart.getShop().getShopId();
            }
        }
        return new CartInfo(cartCount, shopId);

    }

    public Long findShopId(String memberId) {
        Cart findCart = cartRepository.findByMemberId(memberId);
        if (findCart.getShop() != null) {
            return findCart.getShop().getShopId();
        }
        return null;
    }


    @Transactional
    public void setShopCart(String memberId, Long shopId) {
        MemberVO_JPA findMember = memberRepository.findOne(memberId);


        ShopVO shop = shopRepository.findShop(shopId);

        findMember.getCart().setShop(shop);
    }

    @Transactional
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }


    public CartDto findCartByMemberId(String memberId) {
        Cart findCart = cartRepository.findByMemberId(memberId);
        if (findCart != null) {
            if (findCart.getShop() != null) {
                return new CartDto(findCart.getId(),
                        findCart.getShop().getShopId(),
                        findCart.getShop().getMinPriceInt(),
                        findCart.getShop().getDeliveryPrice(),
                        findCart.getCartItems().stream()
                                .map(ci -> new CartItemDto(ci.getId(),
                                        ci.getItem().getItemName(),
                                        getCartItemOptionsName(ci.getCartItemOptions()),
                                        ci.getItem().getItemUrl(),
                                        ci.getQuantity(),
                                        ci.getPrice())).toList(),
                        findCart.getTotalPrice());
            }
            return new CartDto(findCart.getId());
        }
        return null;
    }

    private String getCartItemOptionsName(List<CartItemOption> ci) {
        List<String> list = ci.stream().map(i -> i.getItemOption().getContent()).toList();
        return list.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }
}
