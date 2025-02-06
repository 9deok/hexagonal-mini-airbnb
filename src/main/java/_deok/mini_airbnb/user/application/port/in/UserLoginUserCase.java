package _deok.mini_airbnb.user.application.port.in;

import _deok.mini_airbnb.global.auth.utils.UserDto;
import _deok.mini_airbnb.user.domain.User;
import java.util.Optional;

public interface UserLoginUserCase {
    Optional<User> login(UserDto userDto);
}
