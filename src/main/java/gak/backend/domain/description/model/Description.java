package gak.backend.domain.description.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

import static gak.backend.domain.description.dto.DescriptionDTO.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    //근데 정답자만 볼거면 굳이 문제 번호는 다 똑같을텐데 PathVariable로만 넘겨줘도 될 것 같다.
    public DescriptionSimpleInfoDTO toDescriptionSimpleInfoDTO(Member member){
        return DescriptionSimpleInfoDTO.builder()
                .member_id(member.getId())
                .build();
    }

//
//    public void create(String answer,Boolean quiz, String content){
//        this.answer=answer;
//        this.quiz=quiz;
//        this.content=content;
//    }
    public void updateContent(String content){
        this.content=content;
    }

    public void QuestionSetting(Question question){ this.question=question; }
}
