package _deok.mini_airbnb.user.application.service;

import _deok.mini_airbnb.user.application.port.in.UserSignUpUseCase;
import _deok.mini_airbnb.user.application.port.in.command.UserSignUpCommand;
import _deok.mini_airbnb.user.application.port.out.RegisterUserPort;
import _deok.mini_airbnb.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignUpService implements UserSignUpUseCase {
    private final RegisterUserPort registerUserPort;

    @Override
    public UserSignUpServiceResponse signUp(UserSignUpCommand command) {
        User user = registerUserPort.registerUser(command);
        return UserSignUpServiceResponse.builder()
            .email(user.getEmail())
            .userName(user.getUserName())
            .userRole(user.getUserRole())
            .build();
    }
}
