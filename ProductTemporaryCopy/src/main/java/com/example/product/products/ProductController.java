package com.example.product.products;

import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// final 혹은 @NonNull로 필요한 생성자만 생성하는 어노테이션
@RequiredArgsConstructor
// Rest API 컨트롤러
@RestController

// 앞으로 메소드들의 매핑은 /product 에 경로 매핑
@RequestMapping("/product")
public class ProductController {

    // @RequiredArgsConstructor 에 의해 생성자 생성
    private final ProductService productService;

    // ------- <전체 상품 확인> --------
    // 해당 메소드의 경로는 GET 방식으로 작동된다.
    @GetMapping(value = {"/products", ""})
    /* findAll 메소드는 ResponseEntity<?> 의 타입을 가지고 있으며 ...
     * ResponseEntity<?> = 는 HTTP의 응답을 표현하는데 사용되며 제네릭으로 이루어져있어 ...
     * 응답의 상태 코드, 헤더, 본문 데이터 등을 포함할 수 있다,
     * @RequestParam 은 (page의 매개변수가 0=없을경우 기본값으로 0을 사용하도록 지정)사용해서 page 요청 매개변수를 받아온다. */
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page) {

        // 상품 목록을 조회하고 조회된 상품은 FindAllDTO 타입의 productDTOS 로 저장.
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(page);

        // .success를 호출해서 (productDTOS)를 성공 응답 데이터로 감싼 .ApiResult<?> 객체 생성
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);

        // ResponseEntity.ok 매개변수를 호출하여 apiResult를 성공 상태 코드(200 OK)와 함께 응답으로 반환
        // ResponseEntity에 대한 설명은 상단에 존재
        return ResponseEntity.ok(apiResult);
    }

    // ------- <개별 상품 확인> --------
    // 해당 메소드의 경로는 GET 방식으로 작동된다.
    // {id} 는 @PathVariable 로 가져온 Long 타입의 id
    @GetMapping("/{id}")

    // @PathVariable 은 경로에서 변수값을 추출하는데 사용
    // ex) 사용자가 "/product/1" 경로를 가진 요청이 있으면 ..
    // @PathVariable Long id 매개변수에 123이 할당되어 사용할 수 있다!
    public ResponseEntity<?> findById(@PathVariable Long id) {

        // @PathVariable 로 받아온 변수(ID)로 .findById 메소드를 이용해 (ID)를 이용해 ID를 찾음 ..
        // 조회된 상품은 .FindByIdDTO 타입의 productDTOS 변수에 저장
        ProductResponse.FindByIdDTO productDTOS = productService.findById(id);

        // (productDTOS)를 성공 응답 데이터로 감싼 .ApiResult<?> 객체 생성
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        // apiResult를 성공 상태 코드(200 OK)와 함께 응답으로 반환
        return ResponseEntity.ok(apiResult);
    }

    // ------- <상품 정보 저장> --------
    // 해당 메소드의 경로는 POST 방식으로 작동된다.
    @PostMapping("/save")

    // @RequestBody productResponse를 매개변수로 받아온다 ..
    // @RequestBody는 사용자가 요청 본문에 담아 보낸 데이터를 매개변수로 받아오는 역할
    public ApiUtils.ApiResult<?> save(@RequestBody ProductResponse productResponse) {

        // .toEntity를 호출해서 받아온 productResponse 객체를 엔티티 객체로 변환후 저장
        productService.save(productResponse.toEntity());

        // 성공 메시지
        return ApiUtils.success("상품 등록에 성공했습니다.");
    }

    /* < 참고용 >
    @PostMapping("/save")
    public ResponseEntity<?> createProduct(@RequestBody ProductResponse.CreateDTO createDTO){
        ProductResponse.FindByIdDTO createdProduct = productService.createProduct(createDTO);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(createdProduct);
        return ResponseEntity.ok(apiResult);
    }
     */

    // ------- <상품 정보 수정> --------
    // 해당 메소드의 경로는 POST 방식으로 작동된다.
    @PostMapping("/update/{id}")

    // {id}에 Long id 받아오며,
    // @RequestBody는 사용자가 요청 본문에 담아 보낸 데이터를 매개변수로 받아오는 역할
    public ApiUtils.ApiResult<?> update(@PathVariable Long id, @RequestBody ProductResponse productResponse) {

        // 상품 찾는 메소드 호출하고 매개변수로 @PathVariable 로 받아온 id 전송
        productService.findProductById(id);

        // 받아온 id와 productResponse를 호출하여 상품 업데이트
        productService.update(id, productResponse);

        // 성공 메시지
        return ApiUtils.success("상품 수정에 성공했습니다.");
    }

    // ------- <상품 정보 삭제> --------
    // 해당 메소드의 경로는 DELETE 방식으로 작동된다.
    // @DeleteMapping은 사용자의 DELETE 요청을 처리하는 핸들러 메소드를 지정하는 어노테이션
    @DeleteMapping("/delete/{id}")

    // {id} 에 Long id 받아옴
    public ApiUtils.ApiResult<?> delete(@PathVariable Long id) {

        // 서비스의 delete 메소드 호출하고 매개변수로 사용자가 준 id 사용
        productService.delete(id);

        // 성공 메시지
        return ApiUtils.success("상품 삭제에 성공했습니다.");
    }
}