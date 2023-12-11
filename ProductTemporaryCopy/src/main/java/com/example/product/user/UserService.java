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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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

    public void authenticateWithKakaoToken(String kakaoToken) throws JsonProcessingException {
        // 카카오 토큰을 이용해 사용자 정보를 받아옵니다.
        KakaoInfo kakaoInfo = getUserInfo(kakaoToken);

        // 카카오 ID를 이용해 우리 서버에서 사용자를 조회합니다.
        Optional<User> userOptional = findByKakaoId(kakaoInfo.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 사용자 정보를 이용해 Spring Security 인증 토큰을 생성합니다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // 인증 토큰을 SecurityContext에 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            // 사용자를 찾을 수 없는 경우, 적절한 예외를 던집니다.
            throw new ResponseStatusException(
                    HttpStatus.SEE_OTHER,
                    "사이트에 동록된 사용자 정보를 찾을 수 없습니다. 회원가입을 진행해주세요."
            );
        }
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