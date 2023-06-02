package gak.backend.domain.form.exception;

public class NotFoundCorrException extends RuntimeException {

    public NotFoundCorrException(Long formId) {
        super("form with id "+ formId + "no one responded. ");
    }
}
