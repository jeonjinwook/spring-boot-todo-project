package com.example.demo.user.service;


import com.example.demo.user.dto.ResponseDto;
import com.example.demo.user.dto.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.enums.Authority;
import com.example.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResponseDto responseDto;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private JwtUtil jwtUtil;

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_TYPE = "Bearer ";
    private final String REFRESH_AUTHORIZATION_HEADER = "RefreshAuthorization";

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분

    public ResponseEntity<?> login(Users users, HttpServletResponse response) {

        if (userRepository.findByEmail(users.getEmail()).orElse(null) == null) {
            return responseDto.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = null;

        try {

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    users.getEmail(),
                    users.getPassword());
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Object principal = authentication.getPrincipal();

            UserDetails userDetails = (UserDetails) principal;

            String accessToken = jwtUtil.generateToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME);

            String refreshToken = jwtUtil.generateToken(userDetails, REFRESH_TOKEN_EXPIRE_TIME);

            response.addHeader(AUTHORIZATION_HEADER, BEARER_TYPE + accessToken);

            response.addHeader(REFRESH_AUTHORIZATION_HEADER, BEARER_TYPE + refreshToken);

            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(), refreshToken,
                            REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseDto.success("로그인에 성공했습니다.");
    }

    public ResponseEntity<?> signUp(Users users) {
        if (userRepository.existsByEmail(users.getEmail())) {
            return responseDto.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        Users user = Users.builder()
                .email(users.getEmail())
                .name(users.getName())
                .password(passwordEncoder.encode(users.getPassword()))
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        userRepository.save(user);

        return responseDto.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization").substring(7);

        String userName = jwtUtil.extractUsername(accessToken);

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + userName) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + userName);
        }

        return responseDto.success("로그아웃 되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization").substring(7);

        String userName = jwtUtil.extractUsername(accessToken);

        long userNo = userRepository.findByEmail(userName).get().getUserNo();

        userRepository.deleteById(userNo);

        return responseDto.success("회원탈퇴가 정상적으로 처리되었습니다.");
    }

}
