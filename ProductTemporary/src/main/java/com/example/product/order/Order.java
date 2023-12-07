package com.example.product.order;

import com.example.product.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
/**
 * 이 클래스의 테이블명을 order_tb 로 설정
 * User user 의 컬럼명을 "user_id"로 설정후 이 컬럼에 대해 order_user_id_idx 라는 이름의 인덱스 생성
 */
@Table(name = "order_tb", indexes = {
        @Index(name = "order_user_id_idx", columnList = "user_id")
})
public class Order {

    // PK값으로 사용될 ID
    @Id
    // DB에서 자동 실행되는 기본 키 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Order(Long id, User user) {
        this.id = id;
        this.user = user;
    }
}
