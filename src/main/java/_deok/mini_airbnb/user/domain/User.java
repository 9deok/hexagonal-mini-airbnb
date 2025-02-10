package _deok.mini_airbnb.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private String userRole;

    @Builder
    public User(Long id, String userName, String email, String password, String userRole) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

}
