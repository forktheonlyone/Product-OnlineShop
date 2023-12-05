package com.example.product.option;

import com.example.product.core.error.exception.Exception404;
import com.example.product.products.Product;
import com.example.product.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public List<OptionResponse.FindByProductIdDTO> findByProductId(Long id) {
        List<Option> optionList = optionRepository.findByProductId(id);
        // 받아오는애가 하나가 아니기 때문에 요 값으로 변경되어야 한다. (. 찍을 애들로 변경해야한다)
        List<OptionResponse.FindByProductIdDTO> optionResponses =
                // 타입명의 <OptionResponse>, 리스트의 <OptionResponse>로 변경해줘야한다?
                optionList.stream().map(OptionResponse.FindByProductIdDTO::new)
                        .collect(Collectors.toList());
        return optionResponses;
    }

    public List<OptionResponse.FindAllDTO> findAll() {
        List<Option> optionList = optionRepository.findAll();

        List<OptionResponse.FindAllDTO> findAllDTOS = optionList.stream().map(OptionResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        return findAllDTOS;
    }

    public Long addOption(OptionResponse.FindByProductIdDTO optionResponseFind) {
        Product product = productRepository.findById(optionResponseFind.getProductId()).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다 :" + optionResponseFind.getProductId()) );

        Option option = optionResponseFind.toEntity(product);
        optionRepository.save(option);
        return option.getId();
    }

    public void updateOption(Long optionId, OptionResponse.FindByProductIdDTO optionInfo) {
        Option option = optionRepository.findById(optionId).orElseThrow(
                () -> new Exception404("해당 옵션을 찾을 수 없습니다 : " + optionId));

        Product product = productRepository.findById(optionInfo.getProductId()).orElseThrow(
                () -> new Exception404("해당 옵션을 찾을 수 없습니다 :  " + optionInfo.getProductId()));

        option.update(optionInfo, product);
        optionRepository.save(option);
    }

    public void deleteOption(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("Product not found with id: " + productId));

        Option option = optionRepository.findByIdAndProduct(optionId, product)
                .orElseThrow(() -> new Exception404("Option not found with id: " + optionId + " for product id: " + productId));

        optionRepository.delete(option);
    }



}
