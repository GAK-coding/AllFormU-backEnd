package gak.backend.domain.form.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class FormDTO implements Serializable {


    private Long id;
    private Long authorId;
    private List<QuestionDto> questions;
    private String title;
    private String content;
    private boolean required;


    @Builder
    public FormDTO(Long id, Long authorId, List<QuestionDto> questions,  String title, String content, boolean required){

        this.id=id;
        this.authorId=authorId;
        this.questions=questions;
        this.title=title;
        this.content=content;
        this.required=required;

    }

}
