package com.example.filter;

import com.example.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_TYPE = "Bearer ";
    private final String REFRESH_AUTHORIZATION_HEADER = "RefreshAuthorization";
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
        JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        String refreshHeader = request.getHeader(REFRESH_AUTHORIZATION_HEADER);
        String accessToken = null;
        String refreshToken = null;
        try {

            if ((!ObjectUtils.isEmpty(header)) && (header.startsWith(BEARER_TYPE))) {

                accessToken = header;

                if ((!ObjectUtils.isEmpty(refreshHeader)) && (refreshHeader.startsWith(
                    BEARER_TYPE))) {

                    refreshToken = refreshHeader;

                }

                try {

                    this.jwtUtil.validate(header.substring(7), request);

                } catch (ExpiredJwtException e) {

                    this.jwtUtil.reNewAccessTokenFromRefreshToken(refreshToken, request);

                }

                try {

                    this.jwtUtil.validate(refreshHeader.substring(7), request);

                } catch (ExpiredJwtException e) {

                    this.jwtUtil.reNewRefreshTokenFromAccessToken(accessToken, request);

                }

            }


        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }

        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        response.setHeader(REFRESH_AUTHORIZATION_HEADER, refreshToken);

        chain.doFilter(request, response);
    }


}
