package _deok.mini_airbnb.global.auth.utils;

import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    @Value("${jwt.sercret.key}")
    private SecretKey JWT_SECRET_KEY;

    private LocalDateTime createExpoiredDate() {
        return LocalDateTime.now().plusHours(1);
    }

    private Map<String, Object> createHeader() {
        return Jwts.header()
            .add("typ", "JWT")
            .add("alg", "HS256")
            .add("regDate", LocalDateTime.now())
            .build();
    }
}
