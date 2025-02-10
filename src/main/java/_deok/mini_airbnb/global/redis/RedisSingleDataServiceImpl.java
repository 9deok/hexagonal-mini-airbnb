package _deok.mini_airbnb.global.redis;

import _deok.mini_airbnb.global.config.RedisConfig;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSingleDataServiceImpl implements RedisSingleDataService{

    private final RedisHandler redisHandler;
    private final RedisConfig redisConfig;


    @Override
    public int setSingleData(String key, Object value) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value));
    }

    @Override
    public int setSingleData(String key, Object value, Duration duration) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value, duration));
    }

    @Override
    public String getSingleData(String key) {
        if (redisHandler.getValueOperations().get(key) == null) return "";
        return String.valueOf(redisHandler.getValueOperations().get(key));
    }

    @Override
    public int deleteSingleData(String key) {
        return redisHandler.executeOperation(() -> redisConfig.redisTemplate().delete(key));
    }
}
