package gak.backend.global.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

//일괄된 오류 던져주는 메시지 형식을 위한 class
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
