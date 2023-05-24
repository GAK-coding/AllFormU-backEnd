package gak.backend.domain.form.exception;

public class NotFoundFormException extends RuntimeException{


    public NotFoundFormException(String message) {
        super(message);
    }

    public NotFoundFormException(Long userId) {
        super("with userid " + userId + " does not have any forms");
    }
    public NotFoundFormException(Long userId, Long formId) {
        super("form with id " + formId + " does not have any forms. or must be check exist userId "+userId);
    }
}
