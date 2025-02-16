package _deok.mini_airbnb.global.auth.handler;

import _deok.mini_airbnb.global.auth.service.TokenBlackListService;
import _deok.mini_airbnb.global.auth.token.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenBlackListService tokenBlackListService;
    private final TokenUtils tokenUtils;

    public CustomLogoutHandler(TokenBlackListService tokenBlackListService, TokenUtils tokenUtils) {
        this.tokenBlackListService = tokenBlackListService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        log.info("로그 아웃");
        String headerAccessToken = request.getHeader("Authorization");

        if (headerAccessToken != null) {
            String token = tokenUtils.getTokenOfHeader(headerAccessToken);
            if (!tokenBlackListService.isTokenBlacklisted(token)) {
                tokenBlackListService.addTokenToList(token);
            } else {
                log.error("이미 로그아웃된 토큰입니다.");
                log.info("이미 로그아웃된 토큰입니다.");
            }
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userInfo", null);
            resultMap.put("resultCode", 9999);
            resultMap.put("failMsg", "로그아웃 과정에서 문제가 발생하였습니다.");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = null;
            PrintWriter printWriter = null;
            try {
                jsonResponse = objectMapper.writeValueAsString(resultMap);
                printWriter = response.getWriter();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            printWriter.print(jsonResponse);
            printWriter.flush();
            printWriter.close();
        }
    }
}
