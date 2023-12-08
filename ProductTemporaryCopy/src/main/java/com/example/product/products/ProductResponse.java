package com.example.product.products;

import com.example.product.option.Option;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ProductResponse {
    // PK
    private Long id;

    // 상품명
    private String productName;

    // 상품의 설명
    private String description;

    // 상품의 이미지
    private String image;

    // 상품의 가격
    private int price;

    public Product toEntity() {
        return Product.builder()
                .productName(productName)
                .description(description)
                .image(image)
                .price(price)
                .build();
    }

    public Product updateToEntity(Long id) {
        return Product.builder()
                .id(id)
                .productName(productName)
                .description(description)
                .image(image)
                .price(price)
                .build();
    }

    @Setter
    @Getter
    public static class FindAllDTO {
        // PK
        private Long id;

        // 상품명
        private String productName;

        // 상품의 설명
        private String description;

        // 상품의 이미지
        private String image;

        // 상품의 가격
        private int price;

        public FindAllDTO(Product product) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
        }
    }

    @Setter
    @Getter
    public static class FindByIdDTO {
        // PK
        private Long id;

        // 상품명
        private String productName;

        // 상품의 설명
        private String description;

        // 상품의 이미지
        private String image;

        // 상품의 가격
        private int price;

        private List<OptionDTO> optionList;

        public FindByIdDTO (Product product, List<Option> optionList) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
            this.optionList = optionList.stream().map(OptionDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Setter
    @Getter
    public static class OptionDTO {
        private Long id;
        private String optionName;
        private Long price;
        private Long quantity;

        public OptionDTO(Option option) {
            this.id = option.getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }
}
