package _deok.mini_airbnb.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        List<String> notNeedAuthUrl = List.of(
            "/login"
        );

        if(notNeedAuthUrl.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if(HTTP_METHOD_OPTIONS.equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");


    }
}
