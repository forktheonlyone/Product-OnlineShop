package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.user.User;
import lombok.Getter;
import lombok.Setter;

public class CartRequest {

    // 저장해달라는 요청
    @Setter
    @Getter
    public static class SaveDTO{
        private Long optionId;
        private Long quantity;

        public Cart toEntity(Option option, User user) {
            return Cart.builder()
                    .option(option)
                    .quantity(quantity)
                    .price(option.getPrice() * quantity)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class UpdateDTO{
        private Long cartId;

        private Long quantity;
    }
}
