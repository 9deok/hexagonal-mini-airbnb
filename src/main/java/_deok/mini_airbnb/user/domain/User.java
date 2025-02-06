package _deok.mini_airbnb.user.domain;

import lombok.Getter;

@Getter
public class User {
    private String id;
    private String userName;
    private String email;
    private String password;
    private String userRole;
}
