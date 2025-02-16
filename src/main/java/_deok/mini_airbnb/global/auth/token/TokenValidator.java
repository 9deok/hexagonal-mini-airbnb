package _deok.mini_airbnb.global.auth.token;

import _deok.mini_airbnb.global.auth.service.TokenBlackListService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final TokenUtils tokenUtils;
    private final TokenBlackListService tokenBlackListService;

    public TokenValidateDto isValidToken(String token) {
        if (token == null || token.isBlank()) {
            log.error("Token is null or blank.");
            return TokenValidateDto.builder()
                .isValid(false)
                .errorMessage("TOKEN_NULL")
                .build();
        }

        if(tokenBlackListService.isTokenBlacklisted(token)) {
            log.error("[경고!] 로그 아웃 된 토큰으로 접근 시도!!");
            return TokenValidateDto.builder()
                .isValid(false)
                .errorMessage("LOGOUT_TOKEN")
                .build();
        };

        try {
            Long userId = tokenUtils.getUserIdOfToken(token);
            if (userId == null) {
                log.error("Token does not contain a valid userId.");
                return TokenValidateDto.builder()
                    .isValid(false)
                    .errorMessage("TOKEN_USERID_NULL")
                    .build();
            }

            Claims claims = tokenUtils.getClaimOfToken(token);
            log.info("Valid Token : userId = {} userName = {}",
                claims.get("userId"), claims.get("userName"));
            return TokenValidateDto.builder().isValid(true).build();
        } catch (ExpiredJwtException exception) {
            log.error(exception.getLocalizedMessage());
            log.info("token is expired token : {}", token);
            return TokenValidateDto.builder().isValid(false).errorMessage("TOKEN_EXPIRED").build();
        } catch (JwtException exception) {
            log.error(exception.getLocalizedMessage());
            log.info("token is invalid token : {}", token);
            return TokenValidateDto.builder().isValid(false).errorMessage("TOKEN_INVALID").build();
        } catch (NullPointerException exception) {
            log.error(exception.getLocalizedMessage());
            log.info("token is null token : {}", token);
            return TokenValidateDto.builder().isValid(false).errorMessage("TOKEN_NULL").build();
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            log.info("unknown token Error : {}", token);
            return TokenValidateDto.builder().isValid(false).errorMessage("TOKEN_ERROR").build();
        }
    }
}
