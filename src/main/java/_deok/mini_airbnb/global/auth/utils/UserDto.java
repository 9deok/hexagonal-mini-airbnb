package _deok.mini_airbnb.global.auth.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String password;
    @JsonProperty("username")
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
