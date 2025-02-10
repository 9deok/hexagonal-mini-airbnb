package _deok.mini_airbnb.user.adapter.out.persistence;

import java.util.Optional;

public interface UserQueryDslSupport {

    Optional<UserEntity> findByName(String name);

    UserEntity save(UserEntity userEntity);
}
