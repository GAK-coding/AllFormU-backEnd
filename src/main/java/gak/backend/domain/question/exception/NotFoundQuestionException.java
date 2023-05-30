package gak.backend.domain.question.exception;

public class NotFoundQuestionException extends RuntimeException {
    public NotFoundQuestionException(String message) {
        super(message);
    }

    public NotFoundQuestionException(Long formId) {
        super("Form with id " + formId + " does not have any questions");
    }
    public NotFoundQuestionException(Long formId, Long questionId) {
        super("Question with id " + questionId + " does not have any questions. or must be check exist FormId "+formId);
    }
}
