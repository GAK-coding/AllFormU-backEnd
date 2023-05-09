package gak.backend.domain.member.exception.handler;

import gak.backend.domain.member.exception.ExistMemberException;
import gak.backend.domain.member.exception.NotFoundMemberByEmailException;
import gak.backend.domain.member.exception.NotMatchPasswordException;
import gak.backend.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(NotFoundMemberByEmailException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundMemberByEmail(NotFoundMemberByEmailException e, WebRequest webRequest){
        log.debug("이메일에 해당하는 객체를 찾을 수 없습니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("이메일에 해당하는 객체를 찾을 수 없습니다.")
                .build();
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(ExistMemberException.class)
    protected final ResponseEntity<ErrorResponse> handleExistMember(ExistMemberException e, WebRequest webRequest){
        log.debug("이미 존재하는 회원입니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .message("이미 존재하는 회원입니다.")
                .build();
        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    protected final ResponseEntity<ErrorResponse> handleNotMatchPassword(NotMatchPasswordException e, WebRequest webRequest){
        log.debug("비밀번호가 일치하지 않습니다.");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .message("비밀번호가 일치하지 않습니다.")
                .build();
        return ResponseEntity.ok(errorResponse);
    }
}
