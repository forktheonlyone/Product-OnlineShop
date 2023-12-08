package com.example.product.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
// KakaoService 클래스는 카카오 API와 통신하여 인증 URL과 액세스 토큰을 가져옵니다.
public class KakaoService {

    public String getAuthorizationUrl() {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + "34002e4989051fb4d3de260c6aa8ce65"
                + "&redirect_uri=" + "http://localhost:8080/kakao/callback"
                + "&response_type=code";
        return kakaoUrl;
    }


    public Map<String, String> getAccessToken(String codeOrToken, String grantType) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://kauth.kakao.com/oauth/token?"
                + "grant_type=" + grantType
                + "&client_id=" + "34002e4989051fb4d3de260c6aa8ce65"
                + "&redirect_uri=" + "http://localhost:8080/kakao/callback";
                if (grantType.equals("authorization_code")) {
                    tokenUrl += "&code=" + codeOrToken;
                } else if (grantType.equals("refresh_token")) {
                    tokenUrl += "&refresh_token=" + codeOrToken;
                }

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, null, String.class);

        try {
            // json 파싱을 위한 ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // 응답받은 json을 Map으로 변환
            Map<String, Object> tokenMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", (String) tokenMap.get("access_token"));
            tokens.put("refreshToken", (String) tokenMap.get("refresh_token"));
            return tokens;
            // 액세스 토큰 추출
            //return (String) tokenMap.get("access_token");
        }
        catch (JsonProcessingException e){
            throw new RuntimeException("Access token parsing error", e);
        }
    }
    public Map<String, String> refreshTokens(String refreshToken) {
        log.info("리프레시 토큰으로 액세스 토큰 갱신 중: " + refreshToken);
        Map<String, String> tokenMap = getAccessToken(refreshToken, "refresh_token");
        log.info("액세스 토큰 갱신 완료: " + tokenMap.get("accessToken"));  // 로그 추가
        return tokenMap;
    }

    public KakaoInfo getUserInfo(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Http Request 생성
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        // Http Request 실행
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                String.class);

        // Response Body를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        KakaoInfo kakaoInfo = objectMapper.convertValue(jsonNode, KakaoInfo.class);

        return kakaoInfo;
    }

}
