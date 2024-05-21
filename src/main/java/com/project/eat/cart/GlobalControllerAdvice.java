package com.project.eat.cart;

import com.project.eat.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    @ModelAttribute("cartInfo")
    public CartInfo cartInfo(HttpSession session) {
        String memberId =(String) session.getAttribute("member_id");
        return cartService.cartInfo(memberId);
    }



}
