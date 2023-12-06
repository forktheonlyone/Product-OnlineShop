package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user 별로 카트에 묶임 = 사용자 별로 카트에 묶임
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long price;

    @Builder
    public Cart(Long id, User user, Option option, Long quantity, Long price) {
        this.id = id;
        this.user = user;
        this.option = option;
        this.quantity = quantity;
        this.price = price;
    }

    public void update(Long quantity, Long price) {
        this.quantity = quantity;
        this.price = price;
    }
}