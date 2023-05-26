package gak.backend.domain.description.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Description extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "description_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quesion_id")
    private Question question;

    private String title;
    private String content;
    private String answer;
    private boolean quiz;


    public void create(String answer,Boolean quiz, String content){
        this.answer=answer;
        this.quiz=quiz;
        this.content=content;
    }
    public void updateContent(String content){
        this.content=content;
    }

    public void QuestionSetting(Question question){ this.question=question; }
}
