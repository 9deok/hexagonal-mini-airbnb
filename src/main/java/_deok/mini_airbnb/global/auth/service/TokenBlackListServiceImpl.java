package _deok.mini_airbnb.global.auth.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACK_LIST_KEY = "TOKEN_BLACK_LIST";

    @Override
    public void addTokenToList(String token) {
        redisTemplate.opsForValue().set("BLACKLIST:" + token, "true", 15, TimeUnit.MINUTES);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return "true".equals(redisTemplate.opsForValue().get("BLACKLIST:" + token));
    }

    @Override
    public List<Object> getTokenBlackList() {
        return redisTemplate.opsForList().range(BLACK_LIST_KEY, 0, -1);
    }

    @Override
    public void removeToken(String value) {
        redisTemplate.opsForList().remove(BLACK_LIST_KEY, 0, value);
    }
}
