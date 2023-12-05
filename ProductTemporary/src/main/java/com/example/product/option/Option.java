package com.example.product.option;

import com.example.product.products.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "option_db",
        indexes = {
            @Index(name = "option_product_id_index", columnList = "product_id")
        })

public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    // 옵션 상품 이름, 필수 입력값
    @Column(length = 100, nullable = false)
    private String optionName;

    // 옵션 상품 가격
    private Long price;

    // 옵션 상품 수량
    private Long quantity;

    @Builder
    public Option(Long id, Product product, String optionName, Long price, Long quantity) {
        this.id = id;
        this.product = product;
        this.optionName = optionName;
        this.price = price;
        this.quantity = quantity;
    }


    public void update(OptionResponse.FindByProductIdDTO optionInfo, Product product) {
        this.product = product;
        this.optionName = optionInfo.getOptionName();
        this.price = optionInfo.getPrice();
        this.quantity = optionInfo.getQuantity();
    }

}
