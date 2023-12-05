package com.example.product.products;

import com.example.product.core.error.exception.Exception500;
import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    // 전체 상품 확인
    @GetMapping(value = {"/products", "/"})
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page) {
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    // 개별 상품 확인
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductResponse.FindByIdDTO productDTOS = productService.findById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/save")
    public ApiUtils.ApiResult<?> save(@RequestBody ProductResponse productResponse) {
        productService.save(productResponse.toEntity());
        return ApiUtils.success("상품 등록에 성공했습니다.");
    }

    @PostMapping("/update/{id}")
    public ApiUtils.ApiResult<?> update(@PathVariable Long id, @RequestBody ProductResponse productResponse) {
        Product product = productService.findProductById(id);
        productService.update(id, productResponse);
        return ApiUtils.success("상품 수정에 성공했습니다.");
    }

    @DeleteMapping("/delete/{id}")
    public ApiUtils.ApiResult<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiUtils.success("상품 삭제에 성공했습니다.");
    }
}