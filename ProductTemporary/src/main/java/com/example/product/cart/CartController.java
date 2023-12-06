package com.example.product.cart;

import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    // 카트에 상품 추가
    @PostMapping("/carts/add")
    public ResponseEntity<?> addCartList(
            @RequestBody @Valid List<CartRequest.SaveDTO> requestDTOS,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Error error) {

        cartService.addCartList(requestDTOS, customUserDetails.getUser());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

    // 카트 전체 상품 확인
    @GetMapping("/carts/update")
    public ResponseEntity<?> update(
            @RequestBody @Valid List<CartRequest.UpdateDTO> requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Error error) {
        CartResponse.UpdateDTO updateDTO = cartService.update(requestDTO, customUserDetails.getUser());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updateDTO);
        return ResponseEntity.ok(apiResult);
    }

    // 카트 전체 상품 확인
    @GetMapping("/carts")
    public ResponseEntity<?> carts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CartResponse.FindAllDTO findAllDTO = cartService.findAll();

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAllDTO);
        return ResponseEntity.ok(apiResult);
    }
}
