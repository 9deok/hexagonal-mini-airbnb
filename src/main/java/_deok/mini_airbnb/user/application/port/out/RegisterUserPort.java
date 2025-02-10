package _deok.mini_airbnb.user.application.port.out;

import _deok.mini_airbnb.user.application.port.in.command.UserSignUpCommand;
import _deok.mini_airbnb.user.domain.User;

public interface RegisterUserPort {
    User registerUser(UserSignUpCommand command);
}
