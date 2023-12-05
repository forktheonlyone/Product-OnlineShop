package com.example.product.option;

import com.example.product.products.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class OptionResponse {

    // PK값
    private Long id;

    // 상품
    private Product product;

    // 옵션 상품 이름, 필수 입력값
    private String optionName;

    // 옵션 상품 가격
    private Long price;

    // 옵션 상품 수량
    private Long quantity;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class FindByProductIdDTO {
        // PK값
        private Long id;

        // 상품의 PK값
        private Long productId;

        // 옵션 상품 이름, 필수 입력값
        private String optionName;

        // 옵션 상품 가격
        private Long price;

        // 옵션 상품 수량
        private Long quantity;

        public FindByProductIdDTO(Option option) {
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
        public Option toEntity(Product product) {
            return Option.builder()
                    .product(product)
                    .optionName(optionName)
                    .price(price)
                    .quantity(quantity)
                    .build();
        }
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class FindAllDTO {
        // PK값
        private Long id;

        // 상품의 PK값
        private Long productId;

        // 옵션 상품 이름, 필수 입력값
        private String optionName;

        // 옵션 상품 가격
        private Long price;

        // 옵션 상품 수량
        private Long quantity;

        public FindAllDTO(Option option) {
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }

}
