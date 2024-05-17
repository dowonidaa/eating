package com.project.eat.member;

import com.project.eat.address.AddressVO_JPA;
import com.project.eat.cart.Cart;
import com.project.eat.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class MemberVO_JPA implements Persistable<String> {

     // pk설정
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 전략 설정
    @Column(name = "num") // 컬럼이름 설정
    private int num;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Id
    @Column(name = "member_id", nullable = false)
    private String id;

    //    @Column(name = "member_status", nullable = false)
    @Column(name = "member_status")
    private String status;

    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "email", nullable = true)
    @Column(name = "email")
    private String email;


//    @Column(name = "nickname", nullable = false)
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "pw", nullable = false)
    private String pw;

    private String tel;

    @Column(name = "user_salt")
    private String salt;

    private Date regdate;





    @OneToOne(mappedBy = "member", fetch = LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<Order> orders = new ArrayList<>();

//    @OneToMany(mappedBy = "mId")
//    private List<AddressVO_JPA> address = new ArrayList<>();
//

    @OneToMany(mappedBy = "member")
    private List<Coupon> coupons = new ArrayList<>();

    public void addCart(Cart cart) {
        this.setCart(cart);
        cart.setMember(this);
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
