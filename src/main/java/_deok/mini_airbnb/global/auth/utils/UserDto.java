package _deok.mini_airbnb.global.auth.utils;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String userName;
    private String userRole;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
