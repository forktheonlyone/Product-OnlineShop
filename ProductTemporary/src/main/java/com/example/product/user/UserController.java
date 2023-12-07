package com.example.product.user;

import com.example.product.core.error.exception.Exception401;
import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.security.JwtTokenProvider;
import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error) {

        // 동일한 이메일이 존재하는지 확인
        Optional<User> byEmail = userRepository.findByEmail(requestDTO.getEmail());

        // 존재한다면 에러를 발생
        if (byEmail.isPresent()) {
            throw new Exception401("이미 존재하는 이메일 입니다." + requestDTO.getEmail());
        }

        //이메일 설정 안할꺼면 ( Test용 ) 어쩌피 이건 GET이라 안됨, 여기는 포스트임
        //joinDTO.setEmail("name@email.com");
        //joinDTO.setPassword("1234");

        // password 인코딩 (encording)
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);


        userRepository.save(requestDTO.toEntity());

        return ResponseEntity.ok(ApiUtils.success("null"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error) {

        String jwt = "";

        // 인증 작업
        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            // anonymousUser = 비인증
            Authentication authentication =  authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );

            // ** 인증 완료 값을 받아온다.
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

            // ** 토큰 발급.
            jwt = JwtTokenProvider.create(customUserDetails.getUser());

        }catch (Exception e){
            // 401 반환.
            throw new Exception401("인증되지 않음.");
        }

        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
                .body(ApiUtils.success("null"));
    }
}
