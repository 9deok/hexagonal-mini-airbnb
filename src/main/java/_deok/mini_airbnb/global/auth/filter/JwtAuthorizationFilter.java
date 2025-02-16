package _deok.mini_airbnb.global.auth.filter;

import _deok.mini_airbnb.global.auth.token.TokenGenerator;
import _deok.mini_airbnb.global.auth.token.TokenUtils;
import _deok.mini_airbnb.global.auth.token.TokenValidateDto;
import _deok.mini_airbnb.global.auth.token.TokenValidator;
import _deok.mini_airbnb.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";
    private static final List<String> PUBLIC_URLS =
        List.of(
            "/login",
            "/h2-console","/h2-console/**",
            "/sign-up",
            "/redis/values");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final TokenUtils tokenUtils;
    private final TokenValidator tokenValidator;
    private final TokenGenerator tokenGenerator;

    public JwtAuthorizationFilter(TokenUtils tokenUtils, TokenValidator tokenValidator,
        TokenGenerator tokenGenerator) {
        this.tokenUtils = tokenUtils;
        this.tokenValidator = tokenValidator;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (isPublicUrl(request) || isOptionsRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = tokenUtils.extractToken(request,"Authorization");
            String refreshToken = tokenUtils.extractToken(request,"x-refresh-token");
            TokenValidateDto accessTokenValidateDto = tokenValidator.isValidToken(accessToken);
            if(accessTokenValidateDto.isValid()) {
                Long userId = tokenUtils.getUserIdOfToken(accessToken);
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            } else {
                if(accessTokenValidateDto.getErrorMessage().equals("TOKEN_EXPIRED")) {
                    TokenValidateDto refreshTokenValidateDto = tokenValidator.isValidToken(refreshToken);
                    if(refreshTokenValidateDto.isValid()) {
                        User user = tokenUtils.getUserOfToken(refreshToken, false);
                        String newAccessToken = tokenGenerator.generateToken(user);
                        sendAccessTokenToClient(newAccessToken, response);
                        Long userId = tokenUtils.getUserIdOfToken(refreshToken);
                        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, List.of());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        filterChain.doFilter(request, response);
                    } else {
                        log.error("토큰이 유효하지 않습니다.");
                    }
                } else {
                    log.error("토큰이 유효하지 않습니다.");
                }
            }
        } catch (Exception e) {
            log.error("토큰 검증 실패", e);
            sendErrorResponse(response, e);
        }

    }

    private boolean isPublicUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return PUBLIC_URLS.stream().anyMatch(uri::startsWith);
    }

    private boolean isOptionsRequest(HttpServletRequest request) {
        return HTTP_METHOD_OPTIONS.equalsIgnoreCase(request.getMethod());
    }

    private void sendErrorResponse(HttpServletResponse response, Exception e) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try (PrintWriter out = response.getWriter()) {
            String errorResponse = tokenUtils.createJwtTokenErrorResponse(e);
            out.println(errorResponse);
            out.flush();
        } catch (IOException ioException) {
            log.error("에러 응답 전송 실패", ioException);
        }
    }

    private void sendAccessTokenToClient(String accessToken, HttpServletResponse response) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 401);
        responseBody.put("message", null);
        responseBody.put("accessToken", accessToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(OBJECT_MAPPER.writeValueAsString(responseBody));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            log.error("access Token 결과값 생성 실패 ", e);
        }
    }
}
