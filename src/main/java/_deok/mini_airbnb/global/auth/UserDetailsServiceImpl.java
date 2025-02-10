package _deok.mini_airbnb.global.auth;

import _deok.mini_airbnb.global.auth.utils.UserDetailDto;
import _deok.mini_airbnb.global.auth.utils.UserDto;
import _deok.mini_airbnb.user.application.port.in.UserLoginUserCase;
import _deok.mini_airbnb.user.domain.User;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserLoginUserCase userLoginUserCase;

    public UserDetailsServiceImpl(UserLoginUserCase userLoginUserCase) {
        this.userLoginUserCase = userLoginUserCase;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("사용자 ID가 비어있습니다.");
        }

        UserDto userDto = UserDto.builder()
            .userName(username)
            .email(username)
            .build();

        User loginedUser = userLoginUserCase.login(userDto);
        return new UserDetailDto(loginedUser,
            Collections.singleton(new SimpleGrantedAuthority(loginedUser.getUserRole())));
    }
}
