package gak.backend.global.error;

import gak.backend.global.error.exception.NotFoundByIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(NotFoundByIdException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundIdException(NotFoundByIdException idException, WebRequest webRequest){
        log.debug("해당 아이디로 존재하는 객체를 찾을 수 없습니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("해당 아이디로 존재하는 객체를 찾을 수 없습니다.")
                .build();
        return ResponseEntity.ok(errorResponse);
    }

}
