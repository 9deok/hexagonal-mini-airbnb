package _deok.mini_airbnb.user.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String userName;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Builder
    public UserEntity(Long id, String email, String password, String userName,
        LocalDateTime createdDate, LocalDateTime modifiedDate, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.userRole = userRole;
    }
}
