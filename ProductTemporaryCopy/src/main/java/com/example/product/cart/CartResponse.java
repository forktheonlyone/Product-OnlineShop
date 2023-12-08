package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.products.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    @Setter
    @Getter
    public static class UpdateDTO {
        private List<CartDTO> dtoList;
        private Long totalPrice;

        public UpdateDTO(List<Cart> dtoList) {
            this.dtoList = dtoList.stream().map(CartDTO::new).collect(Collectors.toList());

            this.totalPrice = totalPrice;
        }

        @Setter
        @Getter
        public class CartDTO {
            private Long cartId;

            private Long optionId;

            private String optionName;

            private Long quantity;

            private Long price;

            public CartDTO(Cart cart) {
                this.cartId = cart.getId();
                this.optionId = cart.getOption().getId();
                this.optionName = cart.getOption().getOptionName();
                this.quantity = cart.getQuantity();
                this.price = cart.getPrice();
            }
        }
    }

    @Setter
    @Getter
    public static class FindAllDTO {
        List<ProductDTO> products;

        // 전체 상품 금액
        private Long totalPrice;

        public FindAllDTO(List<Cart> cartList) {
            this.products = cartList.stream()
                    .map(cart -> cart.getOption().getProduct()).distinct()
                    .map(product -> new ProductDTO(cartList, product)).collect(Collectors.toList());
        }

        @Setter
        @Getter
        public class ProductDTO {
            private Long id;

            private String productName;

            List<CartDTO> cartDTOS;

            private Long totalPrice;

            public ProductDTO(List<Cart> cartList, Product product) {
                this.id = product.getId();
                this.productName = product.getProductName();
                this.cartDTOS = cartList.stream()
                        .filter(cart -> cart.getOption().getProduct().getId() == product.getId())
                        .map(CartDTO::new).collect(Collectors.toList());
                // 전체 수량별 금액이 산출이 된다.
                this.totalPrice = cartList.stream()
                        .mapToLong(cart -> cart.getOption().getPrice() * cart.getQuantity())
                        .sum();
            }

            @Setter
            @Getter
            public class CartDTO{
                private Long id;

                private OptionDTO optionDTO;

                // 가격 정보도 필요하니 넣었다.
                private Long price;

                public CartDTO(Cart cart) {
                    this.id = cart.getId();
                    this.optionDTO = new OptionDTO(cart.getOption());
                    this.price = cart.getPrice();
                }

                @Setter
                @Getter
                public class OptionDTO {
                    private Long id;

                    private String optionName;

                    // 가격 정보도 필요하니 넣었다.
                    private Long price;

                    public OptionDTO(Option option) {
                        this.id = option.getId();
                        this.optionName = option.getOptionName();
                        this.price = option.getPrice();
                    }
                }
            }
        }
    }
}

