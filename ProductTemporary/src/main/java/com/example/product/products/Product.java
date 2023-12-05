package com.example.product.products;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // PK NUMBER
    private Long id;

    @Column(length = 100, nullable = false)
    // 상품명, 입력값 필수
    private String productName;

    @Column(length = 1000, nullable = false)
    // 상품의 설명, 입력값 필수
    private String description;

    @Column(length = 100)
    // 상품의 이미지
    private String image;

    // 상품의 가격
    private int price;

    @Builder
    public Product(Long id, String productName, String description, String image, int price) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
    }
}