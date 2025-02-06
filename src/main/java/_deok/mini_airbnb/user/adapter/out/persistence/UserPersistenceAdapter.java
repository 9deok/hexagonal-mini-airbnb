package _deok.mini_airbnb.user.adapter.out.persistence;

import _deok.mini_airbnb.user.application.port.out.FindUserPort;
import _deok.mini_airbnb.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
class UserPersistenceAdapter implements FindUserPort {

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
