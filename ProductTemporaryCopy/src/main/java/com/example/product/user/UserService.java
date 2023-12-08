package com.example.product.user;

import com.example.product.kakao.KakaoInfo;
import com.example.product.kakao.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;
    private final KakaoService kakaoService;


    public Optional<User> findByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User register(UserRequest.JoinDTO joinDTO, String kakaoId) {
        User newUser = joinDTO.toEntity(kakaoId);
        return save(newUser);
    }


    public KakaoInfo getUserInfo(String accessToken) throws JsonProcessingException {
        // 카카오 API에 사용자 정보 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(USER_INFO_REQUEST_URL, request, String.class);

        // 응답 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        // KakaoInfo 객체 생성
        KakaoInfo kakaoInfo = new KakaoInfo();
        kakaoInfo.setId(rootNode.get("id").asText());
        kakaoInfo.setConnected_at(rootNode.get("connected_at").asText());

        return kakaoInfo;
    }
}