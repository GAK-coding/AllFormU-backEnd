package gak.backend.domain.form.dto;

import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.Separator;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import gak.backend.domain.question.dto.QuestionDTO;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Enumerated(EnumType.STRING)
    private Separator separator;

    //private boolean required;
    private boolean fix;

    @Builder
    public FormDTO(Long id, Long authorId, List<QuestionDTO> questions,  String title, String content,boolean fix){

        this.id=id;
        this.fix=fix;
        this.authorId=authorId;
        this.questions=questions;
        this.title=title;
        this.content=content;


    }

    public Form of (){

        return Form.builder()
                .title(title)
                .fix(fix)
                .content(content)
                .build();
    }
    public List<Question> toQuestions(Form form) {

        List<Question> questionList = new ArrayList<>();
        for (QuestionDTO questionDTO : questions) {
            Question question = Question.builder()
                    .form(form)
                    .title(questionDTO.getTitle())
                    .required(questionDTO.isRequired())
                    .sectionNum(questionDTO.getSectionNum())
                    .type(questionDTO.getType())
                    .build();
            questionList.add(question);
        }
        return questionList;
    }



}

