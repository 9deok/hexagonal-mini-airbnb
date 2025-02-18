package _deok.mini_airbnb.global.config;

import _deok.mini_airbnb.global.auth.filter.CustomAuthenticationFilter;
import _deok.mini_airbnb.global.auth.filter.JwtAuthorizationFilter;
import _deok.mini_airbnb.global.auth.handler.CustomAuthFailureHandler;
import _deok.mini_airbnb.global.auth.handler.CustomAuthSuccessHandler;
import _deok.mini_airbnb.global.auth.handler.CustomGoogleLoginSuccessHandler;
import _deok.mini_airbnb.global.auth.handler.CustomLogoutHandler;
import _deok.mini_airbnb.global.auth.utils.CustomAuthenticationProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomAuthSuccessHandler customLoginSuccessHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomGoogleLoginSuccessHandler customGoogleLoginSuccessHandler;

    public WebSecurityConfig(CustomAuthSuccessHandler customLoginSuccessHandler,
        CustomAuthFailureHandler customAuthFailureHandler,
        JwtAuthorizationFilter jwtAuthorizationFilter,
        CustomAuthenticationProvider customAuthenticationProvider,
        CustomLogoutHandler customLogoutHandler,
        CustomGoogleLoginSuccessHandler customGoogleLoginSuccessHandler) {
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customAuthFailureHandler = customAuthFailureHandler;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customLogoutHandler = customLogoutHandler;
        this.customGoogleLoginSuccessHandler = customGoogleLoginSuccessHandler;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .headers(headers -> headers
                .frameOptions(FrameOptionsConfig::disable) // X-Frame-Options 헤더 비활성화
            )
            .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(customAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(this::configureLogout)
            .oauth2Login(oauth2 ->
                oauth2.successHandler(customGoogleLoginSuccessHandler)
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));      // 허용할 오리진
        configuration.setAllowedMethods(List.of("*"));      // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*"));      // 모든 헤더 허용
        configuration.setAllowCredentials(true);                // 인증 정보 허용
        configuration.setMaxAge(3600L);                         // 프리플라이트 요청 결과를 3600초 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);             // 모든 경로에 대해 이 설정 적용
        return source;
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
            authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/login");     // 접근 URL
        customAuthenticationFilter
            .setAuthenticationSuccessHandler(
                customLoginSuccessHandler);    // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter
            .setAuthenticationFailureHandler(
                customAuthFailureHandler);    // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider);
    }

    private void configureLogout(LogoutConfigurer<HttpSecurity> logout) {
        logout
            // 1. 로그아웃 엔드포인트를 지정합니다.
            .logoutUrl("/logout")
            // 2. 엔드포인트 호출에 대한 처리 Handler를 구성합니다.
            .addLogoutHandler(customLogoutHandler)
            // 3. 로그아웃 처리가 완료되었을때 처리를 수행합니다.
            .logoutSuccessHandler((request, response, authentication) -> response.setStatus(
                HttpServletResponse.SC_OK));
    }

}
