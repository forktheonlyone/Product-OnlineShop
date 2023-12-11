package com.example.product.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
//@Table(name="user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 255, nullable = true, unique = true)
    private String kakaoId;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false, unique = true)
    private String userName;

    @Column(length = 255, nullable = true)
    private String address;

    @Column(length = 11, nullable = true)
    private String phoneNumber;

    @Convert(converter = StringArrayConverter.class)
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(Long id, String password, String kakaoId, String email, String userName, String address, String phoneNumber, List<String> roles) {
        this.id = id;
        this.password = password;
        this.kakaoId = kakaoId;
        this.email = email;
        this.userName = userName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.roles = roles == null || roles.isEmpty() ? Collections.singletonList("ROLE_USER") : roles;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
