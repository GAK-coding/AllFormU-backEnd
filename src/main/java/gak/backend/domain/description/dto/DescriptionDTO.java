package gak.backend.domain.description.dto;

public class DescriptionDTO {
    private String content;
    private String answer;
    private Boolean quiz;

    public DescriptionDTO(String content, String answer, Boolean quiz){
        this.content=content;
        this.answer=answer;
        this.quiz=quiz;
    }
}
