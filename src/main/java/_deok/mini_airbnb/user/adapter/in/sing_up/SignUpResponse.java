package _deok.mini_airbnb.user.adapter.in.sing_up;

import _deok.mini_airbnb.user.application.service.UserSignUpServiceResponse;
import lombok.Builder;

@Builder
record SignUpResponse(
    String email,
    String userName,
    String userRole
) {

    static SignUpResponse of(UserSignUpServiceResponse serviceResponse) {
        return SignUpResponse.builder()
            .email(serviceResponse.getEmail())
            .userName(serviceResponse.getUserName())
            .userRole(serviceResponse.getUserRole())
            .build();
    }

}
