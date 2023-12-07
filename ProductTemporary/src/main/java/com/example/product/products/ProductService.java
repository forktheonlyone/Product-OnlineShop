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

// 여기 메소드들은 트랜잭션으로 처리하고 수정안되고 읽기 전용
@Transactional(readOnly = true)
// final 혹은 @NonNull로 필요한 생성자만 생성하는 어노테이션
@RequiredArgsConstructor
// 서비스임을 Spring에 알림
@Service
public class ProductService {

    // @RequiredArgsConstructor 에 의해 생성자 생성
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    // ------- <전체 상품 검색> --------
    // FindAllDTO 타입을 가지는 findAll 메소드이며 매개변수로 int 타입인 page를 받는다.
    // ex) findAll(1) 을 호출하면 1번째 page의 상품목록을 조회하고 ..
    // 그 결과 List<ProductResponse.FindAllDTO> 의 타입의 리스트로 반환한다.
    // 이 List에는 첫번째 페이지의 상품들에 대한 정보가 .FindAllDTO 객체로 담겨있다.
    // List<ProductResponse.FindAllDTO>은 상품 목록을 담고 있는 리스트의 타입을 의미하며
    // findAll 메소드는 이러한 형태의 리스트를 반환함
    public List<ProductResponse.FindAllDTO> findAll(int page) {

        // Pageable은 스프링프레임워크 라이브러리이며 페이징을 위한 인터페이스
        // 매개변수로 page와 3을 전달해서 Pageable에 전달받은 정보를 저장한 객체인 pageable 을 생성함
        Pageable pageable = PageRequest.of(page, 3);

        // pageable 객체를 사용해서 해당 페이지의 Product 객체를 조회
        // 조회 결과는 Page 객체인 productPage 에 저장
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS = productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        return productDTOS;
    }

    // 개별 상품 검색
    public ProductResponse.FindByIdDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다. : " + id) );

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
                () -> new Exception404("해당 상품을 찾을 수 없습니다. : " + id) );

        Product updatedProduct = productResponse.updateToEntity(id);

        productRepository.save(updatedProduct);
    }

    public Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다. : " + id) );
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
