package com.example.product.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 파라미터가 없는 기본생성자 어노테이션
@NoArgsConstructor
// 롬복 게터
@Getter
// 엔티티 클래스임을 스프링이 인지하고 명시함
@Entity
public class Product {

    // PK값임을 나타내기위한 @ID 어노테이션
    @Id
    // 기본키 생성을 DB에게 위임
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // PK NUMBER
    private Long id;

    // 길이 최대 100까지 입력 가능, null이 될수 없다.
    @Column(length = 100, nullable = false)
    // 상품명, 입력값 필수
    private String productName;

    // 길이 최대 1000까지 입력 가능, null이 될수 없다.
    @Column(length = 1000, nullable = false)
    // 상품의 설명, 입력값 필수
    private String description;

    // 길이 최대 100까지 입력 가능)
    @Column(length = 100)
    // 상품의 이미지
    private String image;

    // 상품의 가격
    private int price;

    /** 빌더 패턴 사용 가능
     * 객체를 생성하고 초기화하는 과정을 더욱 유연하고 간편하게 만들어주는 디자인 패턴 <br>
     * 단계마다 매개변수 설정이 가능하다.
     *
     * @param id
     * 상품의 ID
     * @param productName
     * 상품의 이름
     * @param description
     * 상품의 설명
     * @param image
     * 상품의 이미지
     * @param price
     * 상품의 가격
     */
    @Builder
    // Product 엔티티 클래스의 생성자
    public Product(Long id, String productName, String description, String image, int price) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
    }
}