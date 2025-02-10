package _deok.mini_airbnb.user.application.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignUpServiceResponse {
    private String email;
    private String userName;
    private String userRole;

    @Builder
    public UserSignUpServiceResponse(String email, String userName, String userRole) {
        this.email = email;
        this.userName = userName;
        this.userRole = userRole;
    }
}
