package gak.backend.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

//일괄된 오류 메시지 형식을 위한 class
@Getter
public class ErrorResponse {
    private final HttpStatus httpStatus;
    private final String message;

    @Builder
    public ErrorResponse(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }


}
