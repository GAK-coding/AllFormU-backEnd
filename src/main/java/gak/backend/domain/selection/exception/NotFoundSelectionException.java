package gak.backend.domain.selection.exception;

public class NotFoundSelectionException extends RuntimeException {

    public NotFoundSelectionException(String message) {
        super(message);
    }

    public NotFoundSelectionException(Long questionId) {
        super("question with id " + questionId + " does not have any Selection contents");
    }
    public NotFoundSelectionException(Long questionId, Long selectionId) {
        super("Selection with id " + selectionId + " does not have any selection contents. or must be check exist QuestionId "+questionId);
    }


}
