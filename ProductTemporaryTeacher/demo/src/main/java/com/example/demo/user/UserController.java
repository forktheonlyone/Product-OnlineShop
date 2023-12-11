package com.example.demo.user;

import com.example.demo.core.error.exception.Exception400;
import com.example.demo.core.error.exception.Exception401;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.core.utils.ApiUtils;
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
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error){

        // ** 동일한 email이 존재 하는 지 확인.
        Optional<User> byEmail = userRepository.findByEmail(requestDTO.getEmail());

        // ** 존재 한다면 error을 발생
        if(byEmail.isPresent()) {
            throw new Exception400("이미 존재하는 email 입니다." + requestDTO.getEmail());
        }

        // ** password encoding
        //String encodedPassword = passwordEncoder.encode( requestDTO.getPassword());
        String encodedPassword = passwordEncoder.encode( requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);

        // ** 저장
        userRepository.save(requestDTO.toEntity());

        return ResponseEntity.ok( ApiUtils.success(null) );
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Error error){

        String jwt = "";

        // ** 인증 작업.
        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            // ** anonymousUser = 비인증
            Authentication authentication =  authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken);

            // ** 인증 완료 값을 받아온다.
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

            // ** 토큰 발급.
            jwt = JwtTokenProvider.create(customUserDetails.getUser());

        }catch (Exception e){
            // 401 반환.
            throw new Exception401("인증되지 않음.");
        }

        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
                .body(ApiUtils.success(null));
    }
}






























