package gak.backend.domain.description.exception;

public class NotFoundDescriptionException extends RuntimeException{

    public NotFoundDescriptionException(String message) {
        super(message);
    }

    public NotFoundDescriptionException(Long questionId) {
        super("question with id " + questionId + " does not have any description contents");
    }
    public NotFoundDescriptionException(Long questionId, Long DescriptionId) {
        super("Description with id " + DescriptionId + " does not have any Description contents. or must be check exist QuestionId "+questionId);
    }

}
