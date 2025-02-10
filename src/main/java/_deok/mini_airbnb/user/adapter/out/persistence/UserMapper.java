package _deok.mini_airbnb.user.adapter.out.persistence;

import _deok.mini_airbnb.user.domain.User;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

    User toUserDomain(UserEntity userEntity) {
        return User.builder()
            .id(userEntity.getId())
            .email(userEntity.getEmail())
            .password(userEntity.getPassword())
            .userName(userEntity.getUserName())
            .userRole(userEntity.getUserRole().name())
            .build();
    }
}
