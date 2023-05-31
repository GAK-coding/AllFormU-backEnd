package gak.backend.domain.description.exception.handler;

import gak.backend.domain.description.exception.CanNotAccessDescriptionResponse;
import gak.backend.domain.description.exception.CanNotDeleteDescription;
import gak.backend.domain.description.exception.CanNotResponseDescription;
import gak.backend.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class DescriptionExceptionHandler {

    @ExceptionHandler(CanNotDeleteDescription.class)
    protected final ResponseEntity<ErrorResponse> canNotDeleteDescriptionHandler(CanNotDeleteDescription e, WebRequest webRequest){
        log.debug("삭제할 수 있는 권한이 없습니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .message(e.getMessage())
                .build();
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(CanNotResponseDescription.class)
    protected final ResponseEntity<ErrorResponse> canNotResponseDescription(CanNotResponseDescription e, WebRequest webRequest){
        log.debug("이미 응답한 사용자입니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .message(e.getMessage())
                .build();
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(CanNotAccessDescriptionResponse.class)
    protected final ResponseEntity<ErrorResponse> canNotAccessDescriptionHandler(CanNotAccessDescriptionResponse e, WebRequest webRequest){
        log.debug("접근할 수 없는 응답입니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .message(e.getMessage())
                .build();
        return ResponseEntity.ok(errorResponse);
    }


}
