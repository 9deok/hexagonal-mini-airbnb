package _deok.mini_airbnb.user.adapter.out.persistence;

import _deok.mini_airbnb.user.application.port.out.FindUserPort;
import _deok.mini_airbnb.user.domain.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
class UserPersistenceAdapter implements FindUserPort {

    private final UserQueryDslSupport userQueryDslSupport;

    @Override
    public User findUserByEmail(String name) {
        UserEntity byName = userQueryDslSupport.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // entity to domain
        return null;
    }
}
