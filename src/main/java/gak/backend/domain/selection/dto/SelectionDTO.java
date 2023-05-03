package gak.backend.domain.selection.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SelectionDTO {

    private Long testId;
    private String content;
    private Boolean answer;
//    private Boolean quiz;

    @Builder
    public SelectionDTO(Long testId, String content, Boolean answer){
        this.testId=testId;
        this.content=content;
        this.answer=answer;
//        this.quiz=quiz;
    }
}
