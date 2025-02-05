package _deok.mini_airbnb.global.config;

import _deok.mini_airbnb.global.auth.filter.JwtAuthorizationFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterAfter(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)

                .build();

//        http
//            .authorizeRequests(authorizeRequests ->
//                authorizeRequests
//                    .antMatchers("/").permitAll()
//                    .anyRequest().authenticated()
//            )
//            .formLogin(withDefaults());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));      // 허용할 오리진
        configuration.setAllowedMethods(List.of("*"));      // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*"));      // 모든 헤더 허용
        configuration.setAllowCredentials(true);                // 인증 정보 허용
        configuration.setMaxAge(3600L);                         // 프리플라이트 요청 결과를 3600초 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);             // 모든 경로에 대해 이 설정 적용
        return source;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }
}
