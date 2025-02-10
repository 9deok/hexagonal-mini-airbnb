package _deok.mini_airbnb.global.auth.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        log.info("2.CustomAuthenticationProvider");

        UsernamePasswordAuthenticationToken token =
            (UsernamePasswordAuthenticationToken) authentication;

        String userId = token.getName();
        String password = (String) token.getCredentials();

        UserDetailDto userDetailsDto
            = (UserDetailDto) userDetailsService.loadUserByUsername(userId);

        if (!passwordEncoder.matches(password, userDetailsDto.getPassword())) {
            throw new BadCredentialsException(
                userDetailsDto.getUsername() + " user Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetailsDto, password,
            userDetailsDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
