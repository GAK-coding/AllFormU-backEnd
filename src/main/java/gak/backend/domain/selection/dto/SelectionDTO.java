package gak.backend.domain.selection.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SelectionDTO {
    private String content;
    private Boolean answer;
//    private Boolean quiz;

    @Builder
    public SelectionDTO(String content, Boolean answer){
        this.content=content;
        this.answer=answer;
//        this.quiz=quiz;
    }
}
