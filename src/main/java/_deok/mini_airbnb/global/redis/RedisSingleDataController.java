package _deok.mini_airbnb.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisSingleDataController {

    private final RedisSingleDataService redisSingleDataService;

    @GetMapping("/redis/values")
    public ResponseEntity<Object> getValue(@RequestParam("key") String key) {
        String result = redisSingleDataService.getSingleData(key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/redis/values")
    public ResponseEntity<Object> setValue(@RequestBody RedisDto redisDto) {
        int result = 0;
        if (redisDto.getDuration() == null) {
            result = redisSingleDataService.setSingleData(redisDto.getKey(), redisDto.getValue());
        } else {
            result = redisSingleDataService.setSingleData(redisDto.getKey(), redisDto.getValue(), redisDto.getDuration());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/redis/values")
    public ResponseEntity<Object> deleteRow(@RequestParam("key") String key) {
        int result = redisSingleDataService.deleteSingleData(key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
