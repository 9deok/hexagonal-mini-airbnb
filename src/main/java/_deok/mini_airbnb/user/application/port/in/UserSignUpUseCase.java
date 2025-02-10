package _deok.mini_airbnb.user.application.port.in;

import _deok.mini_airbnb.user.application.port.in.command.UserSignUpCommand;
import _deok.mini_airbnb.user.application.service.UserSignUpServiceResponse;

public interface UserSignUpUseCase {

    UserSignUpServiceResponse signUp(UserSignUpCommand command);
}
