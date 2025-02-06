package _deok.mini_airbnb.global.auth.utils;

public record JwtErrorResponse(
    int status,
    String code,
    String message,
    String error
) {}
