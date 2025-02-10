package _deok.mini_airbnb.user.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserQueryDslSupportImpl implements UserQueryDslSupport {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;
    private final QUserEntity qUserEntity;


    public UserQueryDslSupportImpl(EntityManager entityManager, JPAQueryFactory query) {
        this.entityManager = entityManager;
        this.query = query;
        this.qUserEntity = QUserEntity.userEntity;
    }

    @Override
    public Optional<UserEntity> findByName(String name) {
        return Optional.ofNullable(query.selectFrom(qUserEntity)
            .where(qUserEntity.userName.eq(name))
            .fetchOne());
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

}
