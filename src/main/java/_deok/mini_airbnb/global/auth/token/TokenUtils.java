package _deok.mini_airbnb.global.auth.token;

import _deok.mini_airbnb.global.auth.utils.JwtErrorResponse;
import _deok.mini_airbnb.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenUtils {

    private static SecretKey JWT_SECRET_KEY;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public TokenUtils(@Value("${jwt.secret.key}") String jwtSecretKey) {
        TokenUtils.JWT_SECRET_KEY
            = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String getTokenOfHeader(String header) {
        return header.replace("Bearer ", "");
    }

    public Long getUserIdOfToken(String token) {
        Claims claims = getClaimOfToken(token);
        return claims.get("userId", Long.class);
    }

    public Claims getClaimOfToken(String token) {
        return Jwts.parser()
            .verifyWith(JWT_SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public User getUserOfToken(String token, boolean isAccessToken) {
        Claims claims = getClaimOfToken(token);

        if(isAccessToken) {
            User.builder()
                .id(claims.get("userId", Long.class))
                .userName(claims.get("userName", String.class))
                .userRole(claims.get("userRole", String.class))
                .build();
        }

        return User.builder()
            .id(claims.get("userId", Long.class))
            .build();
    }

    public String createJwtTokenErrorResponse(Exception e) {
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

    public String extractToken(HttpServletRequest request, String headerKey) throws Exception {
        String header = request.getHeader(headerKey);
        if (StringUtils.isBlank(header)) {
            throw new Exception("토큰이 없습니다.");
        }

        return getTokenOfHeader(header);
    }

}
