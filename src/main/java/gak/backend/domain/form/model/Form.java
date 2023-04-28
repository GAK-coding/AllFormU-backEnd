package gak.backend.domain.form.model;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Form extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "form_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "form")
    private List<Question> questions = new ArrayList<>();

    private String title;
    private String content;
    private boolean required;

    // =========변경 가능-----------
//    @ManyToOne
//    @JoinColumn(name = "form_id")
//    private Form parent;
//    @OneToMany(mappedBy = "parent") //cascade = CascadeType.ALL, orphanRemoval = true) 변경 가능
//    private List<Form> child = new ArrayList<>();
    //==========================

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
