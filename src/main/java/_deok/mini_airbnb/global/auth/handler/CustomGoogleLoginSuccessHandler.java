package _deok.mini_airbnb.global.auth.handler;

import _deok.mini_airbnb.global.auth.token.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomGoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenGenerator tokenGenerator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        System.out.println("******************************");
        System.out.println("request = " + request);
        System.out.println("******************************");

//        String accessToken = tokenGenerator.generateToken(user);
//        String refreshToken = tokenGenerator.generateRefreshToken(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("accessToken", "test");
        responseMap.put("refreshToken", "test2");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter printWriter = response.getWriter();
        printWriter.write(objectMapper.writeValueAsString(responseMap));
        printWriter.flush();
        printWriter.close();

    }
}
