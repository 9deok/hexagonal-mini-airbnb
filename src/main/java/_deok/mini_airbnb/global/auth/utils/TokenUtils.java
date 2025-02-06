package _deok.mini_airbnb.global.auth.utils;

import _deok.mini_airbnb.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenUtils {

    @Value("${jwt.sercret.key}")
    private static SecretKey JWT_SECRET_KEY;

    private static Date createExpoireDate() {
        LocalDateTime expireDate = LocalDateTime.now().plusHours(1);
        Instant instant = expireDate.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    private static Map<String, Object> createHeader() {
        return Jwts.header()
            .add("typ", "JWT")
            .add("alg", "HS256")
            .add("regDate", LocalDateTime.now())
            .build();
    }

    private static Map<String, Object> createClaims(User userDto) {
        return Map.of(
            "userId", userDto.getId(),
            "email", userDto.getEmail(),
            "userName", userDto.getUserName()
        );
    }

    public static boolean isValidToken(String token) {
        try {
            Claims claims = getTokenToClaims(token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.error(exception.getLocalizedMessage());
            log.info("token is expired token : {}", token);
            return false;
        } catch (JwtException exception) {
            log.error(exception.getLocalizedMessage());
            log.info("token is invalid token : {}", token);
            return false;
        } catch (NullPointerException exception) {
            log.error(exception.getLocalizedMessage());
            log.info("token is null token : {}", token);
            return false;
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            log.info("unknown token Error : {}", token);
            return false;
        }
    }

    private static Claims getTokenToClaims(String token) {
        return Jwts.parser()
            .verifyWith(JWT_SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public static String generateJwt(User userDto) {
        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(userDto))
            .setExpiration(createExpoireDate())
            .signWith(JWT_SECRET_KEY)
            .compact();
    }

    private static Date createrefreshTokenExpireDate() {
        LocalDate tokenExpireDate = LocalDate.now().plusDays(14);
        Instant instant = tokenExpireDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static String generateRefreshToken(User userDto) {
        log.info("generateRefreshToken");
        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(userDto))
            .setExpiration(createrefreshTokenExpireDate())
            .signWith(JWT_SECRET_KEY)
            .compact();
    }

    public static String getHeaderToToken(String header) {
        return header.replace("Bearer ", "");
    }

    public static String getClaimsToUserId(String token) {
        Claims claims = getTokenToClaims(token);
        return claims.get("userId", String.class);
    }
}
