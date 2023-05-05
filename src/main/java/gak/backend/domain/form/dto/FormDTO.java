package gak.backend.domain.form.dto;

import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import gak.backend.domain.question.dto.QuestionDTO;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDTO implements Serializable {


    private Long id;
    private Long authorId;
    private List<QuestionDTO> questions;
    private String title;
    private String content;
    private boolean required;
    @Enumerated
    private Status status;

    //public FormDTO(){}

    @Builder
    public FormDTO(Long id, Long authorId, List<QuestionDTO> questions,  String title, String content, boolean required){

        this.id=id;
        this.authorId=authorId;
        this.questions=questions;
        this.title=title;
        this.content=content;
        this.required=required;

    }

//    public Form of (){
//        return Form.builder()
//                .title(title)
//                .content(content)
//                .build();
//
//
//    }
}
