package com.example.product.kakao;

import com.example.product.user.User;
import com.example.product.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/kakao")
// KakaoController 클래스는 카카오 로그인과 관련된 요청을 처리합니다.
public class KakaoController{

    private final KakaoService kakaoService;
    private final UserService userService;

    // "/kakao/login" 요청이 오면 카카오 인증 페이지로 리다이렉트합니다.
    @GetMapping("/login")
    public String login(HttpSession session) {
        // 세션에 토큰이 있는지 확인
        if (session.getAttribute("accessToken") != null){
            // "accessToken"이 있다면 사용자는 이미 로그인한 상태
            // 따라서 메인 페이지로 리다이렉트
            return "redirect:/home";
        }
        else {
            // "accessToken"이 없다면 사용자는 로그인하지 않은 상태
            // 따라서 카카오 로그인 페이지로 이동
            String url = kakaoService.getAuthorizationUrl();
            return "redirect:" + url;
        }
    }

    // 카카오 인증 후 리다이렉트되는 "/kakao/callback" 요청을 처리합니다.
    // 인증 코드를 사용하여 액세스 토큰을 가져와 세션에 저장합니다.
    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam("code") String code, HttpSession session) throws JsonProcessingException {
        Map<String, String> tokenMap = kakaoService.getAccessToken(code, "authorization_code");
        String accessToken = tokenMap.get("accessToken");
        String refreshToken = tokenMap.get("refreshToken");
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);
        log.info("리프레시 토큰 저장: " + tokenMap.get("refreshToken"));

        KakaoInfo kakaoInfo = kakaoService.getUserInfo(accessToken);
        String kakaoId = kakaoInfo.getId();

        // 이미 가입된 사용자인지 확인
        Optional<User> existingUser = userService.findByKakaoId(kakaoId);
        if (existingUser.isPresent()) {
            return "redirect:/home";
        }
        else {
            // 가입되지 않은 사용자라면, 간편가입 페이지로 이동
            return "redirect:/register";
        }
    }

    // "/kakao/logout" 요청이 오면 세션을 초기화하고 메인 페이지로 리다이렉트합니다.
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setMaxInactiveInterval(0);
        session.invalidate();
        return "redirect:/login";
    }
}
