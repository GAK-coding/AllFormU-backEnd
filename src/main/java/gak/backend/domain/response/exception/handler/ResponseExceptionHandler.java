package gak.backend.domain.response.exception.handler;

import gak.backend.domain.response.exception.CanNotAccessResponse;
import gak.backend.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

//TODO message 매개변수로 넘기는 걸로 더 세분화하기
@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(CanNotAccessResponse.class)
    protected final ResponseEntity<ErrorResponse> CanNotAccessResponseHandler(CanNotAccessResponse e, WebRequest webRequest){
        log.debug("설문을 응답할 수 없습니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .message("설문 수정을 할 수 없습니다.")
                .build();
        return ResponseEntity.ok(errorResponse);
    }
}
