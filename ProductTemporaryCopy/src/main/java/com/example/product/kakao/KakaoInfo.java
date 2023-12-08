package com.example.product.kakao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class KakaoInfo {
    // 카카오 사용자 ID 저장하는 필드
    private String id;
    private String connected_at;
}
