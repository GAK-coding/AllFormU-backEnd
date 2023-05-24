package gak.backend.domain.form.exception;

public class NotFoundMemberException extends RuntimeException{

    public NotFoundMemberException(Long userId) {
        super("user id " + userId + " does not exist");
    }
}
