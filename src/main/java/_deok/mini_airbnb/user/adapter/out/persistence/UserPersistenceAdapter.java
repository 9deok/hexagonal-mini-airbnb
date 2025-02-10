package _deok.mini_airbnb.user.adapter.out.persistence;

import _deok.mini_airbnb.user.application.port.in.command.UserSignUpCommand;
import _deok.mini_airbnb.user.application.port.out.FindUserPort;
import _deok.mini_airbnb.user.application.port.out.RegisterUserPort;
import _deok.mini_airbnb.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
class UserPersistenceAdapter implements FindUserPort, RegisterUserPort {

    private final UserQueryDslSupport userQueryDslSupport;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User findUserByEmail(String name) {
        UserEntity userEntity = userQueryDslSupport.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userMapper.toUserDomain(userEntity);
    }

    @Override
    public User registerUser(UserSignUpCommand command) {
        UserEntity userEntity = UserEntity.builder()
            .email(command.getEmail())
            .userName(command.getUserName())
            .password(passwordEncoder.encode(command.getPassword()))
            .userRole(UserRole.ROLE_USER)
            .build();
        userQueryDslSupport.save(userEntity);

        return userMapper.toUserDomain(userEntity);
    }
}
