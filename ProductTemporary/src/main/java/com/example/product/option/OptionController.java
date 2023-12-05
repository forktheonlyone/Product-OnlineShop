package com.example.product.option;

import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OptionController {

    private final OptionService optionService;

    // 개별 옵션 검색
    /**
    * @param id
     * id 에 관련된 설명 (ProductId)
    * @return
     * {@code List<OptionResponse.FindByProductIdDTO>} 리스트로 반환.
     */
    @GetMapping("/product/{id}/options")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        List<OptionResponse.FindByProductIdDTO> optionResponses = optionService.findByProductId(id);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    // 전체 옵션 검색
    @GetMapping("/options")
    public ResponseEntity<?> findAll() {
        List<OptionResponse.FindAllDTO> optionResponses =
                optionService.findAll();

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/product/{id}/optionscreate")
    public ApiUtils.ApiResult<?> addOption(@PathVariable Long id, @RequestBody OptionResponse.FindByProductIdDTO optionResponseFind) {
        optionResponseFind.setProductId(id);
        optionService.addOption(optionResponseFind);
        return ApiUtils.success("옵션이 성공적으로 등록되었습니다.");
    }

    @PutMapping("/product/{productId}/option/{optionId}")
    public ApiUtils.ApiResult<?> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionResponse.FindByProductIdDTO optionResponseFind) {
        optionResponseFind.setProductId(productId);
        optionService.updateOption(optionId, optionResponseFind);
        return ApiUtils.success("해당 옵션이 성공적으로 수정 되었습니다.");
    }

    @DeleteMapping("/product/{productId}/option/{optionId}")
    public ApiUtils.ApiResult<?> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ApiUtils.success("해당 옵션이 성공적으로 삭제 되었습니다.");
    }



}
