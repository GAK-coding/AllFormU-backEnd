package gak.backend.domain.selection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.dto.SelectionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Selection extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "selection_id")
    private Long id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    //객관식일때 각 보기의 내용들
    private String content;


    //퀴즈형식일때 퀴즈의 정답을 표시해주는거
    private boolean answer;

    //퀴즈형식인지 아닌지
//    private boolean quiz;

    public void createLinear(Question question,String content){
        this.question=question;
        this.content=content;
//      this.answer=answer;
//      this.quiz=quiz;
    }
    public SelectionDTO.SelectionInfoDTO SelectionInfoDTO(){
        return SelectionDTO.SelectionInfoDTO.builder()
                .id(this.id)
                .content(this.content)
                .build();
    }
    public void updateContent(String content){
        this.content=content;
    }

    public void updateAnswer(Boolean answer){
        this.answer=answer;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
}
