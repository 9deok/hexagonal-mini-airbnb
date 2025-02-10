package _deok.mini_airbnb.user.adapter.in.sing_up;

import _deok.mini_airbnb.user.application.port.in.UserSignUpUseCase;
import _deok.mini_airbnb.user.application.port.in.command.UserSignUpCommand;
import _deok.mini_airbnb.user.application.service.UserSignUpServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final UserSignUpUseCase useCase;

    @PostMapping("/sign-up")
    public SignUpResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        UserSignUpCommand command = signUpRequest.toCommand();

        UserSignUpServiceResponse userSignUpServiceResponse = useCase.signUp(command);
        return SignUpResponse.of(userSignUpServiceResponse);
    }
}
