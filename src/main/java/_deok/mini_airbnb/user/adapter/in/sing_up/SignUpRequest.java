package _deok.mini_airbnb.user.adapter.in.sing_up;

import _deok.mini_airbnb.user.application.port.in.command.UserSignUpCommand;
import lombok.Builder;

@Builder
record SignUpRequest(
    String email,
    String userName,
    String password
) {

    UserSignUpCommand toCommand() {
        return UserSignUpCommand.builder()
            .email(email)
            .userName(userName)
            .password(password)
            .userRole("ROLE_USER")
            .build();
    }

}
