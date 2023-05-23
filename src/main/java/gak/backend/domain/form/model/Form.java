package gak.backend.domain.form.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "form_id")
    private Long id;

    public void setAuthor(Member author) {
        this.author = author;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "form",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private Separator separator;

    private String title;
    private String content;


    //private boolean required;

    private boolean fix; // 수정가능 : 0 수정 불가능 : 1

    public void AuthorSetting(Member author){
        this.author=author;
    }

    public void UpdateSelectForm(FormDTO formDTO){
        this.content=(formDTO.getContent()!=null) ? formDTO.getContent() : this.content;
        this.fix=(formDTO.getFix()!=null) ? formDTO.getFix() : this.fix;
        this.title=(formDTO.getTitle()!=null) ? formDTO.getTitle() : this.title;
    }


    public void QuestionSetting(List<Question> questions) {
        this.questions = questions;
    }
    public void FixSetting(boolean fix){
        this.fix=fix;
    }
    public void SeparatorSetting(Separator separator){
        this.separator=separator;
    }

    // =========변경 가능-----------
//    @ManyToOne
//    @JoinColumn(name = "form_id")
//    private Form parent;
//    @OneToMany(mappedBy = "parent") //cascade = CascadeType.ALL, orphanRemoval = true) 변경 가능
//    private List<Form> child = new ArrayList<>();
    //==========================

}
