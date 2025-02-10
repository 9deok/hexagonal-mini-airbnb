package _deok.mini_airbnb.global.redis;

import java.time.Duration;
import lombok.Getter;

@Getter
public class RedisDto {

    private String key;
    private Object value;
    private Duration duration;
}
