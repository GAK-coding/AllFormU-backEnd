package gak.backend.domain.form.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Setter
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


    @Enumerated
    @Column(name = "user_status")
    private Status status;
    private String title;
    private String content;
    private boolean required;

    private boolean fix; // 수정가능 : 0 수정 불가능 : 1


//    public static QForm form=QForm.form;
    // =========변경 가능-----------
//    @ManyToOne
//    @JoinColumn(name = "form_id")
//    private Form parent;
//    @OneToMany(mappedBy = "parent") //cascade = CascadeType.ALL, orphanRemoval = true) 변경 가능
//    private List<Form> child = new ArrayList<>();
    //==========================


//    public Long getAuthorId(){
//        return author.getId();
//    }
}
