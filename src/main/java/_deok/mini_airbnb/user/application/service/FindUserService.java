package _deok.mini_airbnb.user.application.service;

import _deok.mini_airbnb.global.auth.utils.UserDto;
import _deok.mini_airbnb.user.application.port.in.UserLoginUserCase;
import _deok.mini_airbnb.user.application.port.out.FindUserPort;
import _deok.mini_airbnb.user.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindUserService implements UserLoginUserCase {

    private final FindUserPort findUserPort;

    @Override
    public Optional<User> login(UserDto userDto) {
        return null;
    }
}
