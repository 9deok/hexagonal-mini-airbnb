package _deok.mini_airbnb.global.auth.utils;

import java.time.LocalDateTime;
import lombok.Builder;
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

    @Builder
    public UserDto(Long id, String email, String password, String userName, String userRole,
        LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
