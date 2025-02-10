package _deok.mini_airbnb.global.auth.token;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenValidateDto {

    private boolean isValid;
    private String errorMessage;

    @Builder
    public TokenValidateDto(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }
}
