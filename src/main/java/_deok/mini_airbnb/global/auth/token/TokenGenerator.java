package _deok.mini_airbnb.global.auth.token;

import _deok.mini_airbnb.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
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
public class TokenGenerator {

    private static SecretKey JWT_SECRET_KEY;


    public TokenGenerator(@Value("${jwt.secret.key}") String jwtSecretKey) {
        TokenGenerator.JWT_SECRET_KEY = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User userDto) {
        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(userDto))
            .setExpiration(createExpoireDate())
            .signWith(JWT_SECRET_KEY)
            .compact();
    }

    private Date createExpoireDate() {
//        LocalDateTime expireDate = LocalDateTime.now().plusHours(1);
        LocalDateTime expireDate = LocalDateTime.now().plusMinutes(5);
        Instant instant = expireDate.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    public String generateRefreshToken(User userDto) {
        log.info("generateRefreshToken");
        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(userDto))
            .setExpiration(createrefreshTokenExpireDate())
            .signWith(JWT_SECRET_KEY)
            .compact();
    }

    private Map<String, Object> createHeader() {
        return Jwts.header()
            .add("typ", "JWT")
            .add("alg", "HS256")
            .add("regDate", LocalDateTime.now().toString())
            .build();
    }

    private Map<String, Object> createClaims(User userDto) {
        return Map.of(
            "userId", userDto.getId(),
            "email", userDto.getEmail(),
            "userName", userDto.getUserName()
        );
    }

    private static Date createrefreshTokenExpireDate() {
        LocalDate tokenExpireDate = LocalDate.now().plusDays(14);
        Instant instant = tokenExpireDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

}
