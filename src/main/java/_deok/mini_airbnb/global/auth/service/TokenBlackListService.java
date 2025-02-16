package _deok.mini_airbnb.global.auth.service;

import java.util.List;

public interface TokenBlackListService {

    void addTokenToList(String value);              // Redis key-value 형태로 리스트 추가

    boolean isTokenBlacklisted(String value);           // Redis key 기반으로 리스트 조회

    List<Object> getTokenBlackList();               // Redis Key 기반으로 BlackList를 조회합니다.

    void removeToken(String value);
}
