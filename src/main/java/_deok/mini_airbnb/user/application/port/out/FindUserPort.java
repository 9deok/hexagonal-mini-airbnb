package _deok.mini_airbnb.user.application.port.out;

import _deok.mini_airbnb.user.domain.User;

public interface FindUserPort {
    User findUserByEmail(String email);
}
