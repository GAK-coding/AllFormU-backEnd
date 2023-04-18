package gak.backend.domain.question.model;

import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.option.Option;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;

    @OneToMany(mappedBy = "question")
    private List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private List<Description> descriptions = new ArrayList<>();

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private Format type;
}
