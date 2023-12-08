package com.example.product.user;

import com.example.product.core.utils.ApiUtils;
import com.example.product.kakao.KakaoInfo;
import com.example.product.kakao.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> mainPage(HttpSession session) {
        String page = session.getAttribute("accessToken") != null ? "home" : "login";
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(page);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(HttpSession session) throws JsonProcessingException {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            KakaoInfo kakaoInfo = kakaoService.getUserInfo(accessToken);
            Optional<User> userOptional = userService.findByKakaoId(kakaoInfo.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                ApiUtils.ApiResult<?> apiResult = ApiUtils.success(user);
                return ResponseEntity.ok(apiResult);
            }
        }
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success("login");
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/register")
    public ResponseEntity<?> registerForm(HttpSession session) throws JsonProcessingException {
        String accessToken = (String) session.getAttribute("accessToken");
        KakaoInfo kakaoInfo = kakaoService.getUserInfo(accessToken);

        String kakaoId = kakaoInfo.getId();
        log.info("kakaoId: {}", kakaoId);
        Optional<User> existingUser = userService.findByKakaoId(kakaoId);
        if (existingUser.isPresent()) {
            ApiUtils.ApiResult<?> apiResult = ApiUtils.success("home");
            return ResponseEntity.ok(apiResult);
        }
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success("register");
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest.JoinDTO joinDTO, BindingResult result, HttpSession session) throws JsonProcessingException {

        if (result.hasErrors()) {
            ApiUtils.ApiResult<?> apiResult = ApiUtils.success("register");
            return ResponseEntity.ok(apiResult);
        }

        String accessToken = (String) session.getAttribute("accessToken");
        KakaoInfo kakaoInfo = kakaoService.getUserInfo(accessToken);
        String kakaoId = kakaoInfo.getId();
        User user = joinDTO.toEntity(kakaoId);

        userService.save(user);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success("home");
        return ResponseEntity.ok(apiResult);
    }
}
