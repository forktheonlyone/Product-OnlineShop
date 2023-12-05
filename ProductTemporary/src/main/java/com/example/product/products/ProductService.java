package com.example.product.products;

import com.example.product.core.error.exception.Exception404;
import com.example.product.option.Option;
import com.example.product.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    // 전체 상품 검색
    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS = productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        return productDTOS;
    }

    // 개별 상품 검색
    public ProductResponse.FindByIdDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다." + id) );

        // product.getId() 로 Option 상품을 검색.
        List<Option> optionList = optionRepository.findByProductId(product.getId());

        // 검색이 완료된 제품 반환.
        return new ProductResponse.FindByIdDTO(product, optionList);
    }

    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void update(Long id, ProductResponse productResponse) {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다." + id) );

        Product updatedProduct = productResponse.updateToEntity(id);

        productRepository.save(updatedProduct);
    }

    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다." + id) );
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
