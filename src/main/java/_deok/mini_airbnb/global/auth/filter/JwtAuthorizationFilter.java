package _deok.mini_airbnb.global.auth.filter;

import _deok.mini_airbnb.global.auth.utils.JwtErrorResponse;
import _deok.mini_airbnb.global.auth.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";
    private static final List<String> PUBLIC_URLS = List.of("/login", "/h2-console","/h2-console/");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (isPublicUrl(request) || isOptionsRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            validateToken(token);
            // 토큰 유효하면, 인증/권한 관련 추가 처리가 있을 수 있음 (SecurityContext 설정 등)
            filterChain.doFilter(request, response);
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

    private String extractToken(HttpServletRequest request) throws Exception {
        String header = request.getHeader("Authorization");
        if (StringUtils.isBlank(header)) {
            throw new Exception("토큰이 없습니다.");
        }

        return TokenUtils.getHeaderToToken(header);
    }

    private void validateToken(String token) throws Exception {
        if (!TokenUtils.isValidToken(token)) {
            throw new Exception("토큰이 유효하지 않습니다.");
        }
        String userId = TokenUtils.getClaimsToUserId(token);
        if (StringUtils.isBlank(userId)) {
            throw new Exception("토큰 내 사용자 아이디가 없습니다.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, Exception e) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try (PrintWriter out = response.getWriter()) {
            String jsonResponse = jwtTokenError(e);
            out.println(jsonResponse);
            out.flush();
        } catch (IOException ioException) {
            log.error("에러 응답 전송 실패", ioException);
        }
    }

    private String jwtTokenError(Exception e) {
        String resultMessage = switch (e) {
            case ExpiredJwtException ignored -> "토큰이 만료되었습니다.";
            case JwtException ignored -> "토큰이 유효하지 않습니다.";
            default -> "토큰 검증 오류";
        };

        JwtErrorResponse errorResponse
            = new JwtErrorResponse(403, "JWT-403", resultMessage, e.getMessage());

        try {
            return OBJECT_MAPPER.writeValueAsString(errorResponse);
        } catch (Exception ex) {
            log.error("JSON 변환 오류", ex);
            return "{}";
        }
    }
}
