package _deok.mini_airbnb.global.auth.handler;

import _deok.mini_airbnb.global.auth.utils.TokenUtils;
import _deok.mini_airbnb.global.auth.utils.UserDetailDto;
import _deok.mini_airbnb.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Slf4j
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws ServletException, IOException {
        log.debug("3.1. CustomLoginSuccessHandler");

        User user = ((UserDetailDto) authentication.getPrincipal()).getUserDto();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("userInfo", user);

        // [STEP3] 사용자의 상태에 따라 응답 데이터를 설정합니다.
        if ("D".equals(user.getUserRole())) {
            responseMap.put("resultCode", 9001);
            responseMap.put("token", null);
            responseMap.put("failMsg", "휴면 계정입니다.");
        } else {
            responseMap.put("resultCode", 200);
            responseMap.put("failMsg", null);

            String token = TokenUtils.generateJwt(user);
            responseMap.put("token", token);
            response.addHeader("Authorization", "BEARER " + token);
        }

        // [STEP4] 구성한 응답 값을 JSON 형태로 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(objectMapper.writeValueAsString(responseMap));
        printWriter.flush();
        printWriter.close();
    }
}
