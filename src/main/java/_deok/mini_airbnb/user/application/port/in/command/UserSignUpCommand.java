package _deok.mini_airbnb.user.application.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignUpCommand {
    private String email;
    private String userName;
    private String password;
    private String userRole;

    @Builder
    public UserSignUpCommand(String email, String userName, String password, String userRole) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
    }
}
