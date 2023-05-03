package gak.backend.domain.question.model;

import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.grid.model.Grid;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
//TODO img 추가해야함.

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
    private List<Selection> options = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private List<Description> descriptions = new ArrayList<>();

    @OneToMany(mappedBy="question")
    private List<Grid> grids = new ArrayList<>();

    private String title;
    private String content;
    private boolean required;
    //private int number; 설문 순서 -> 프론트랑 상의 해볼것.

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private Format type;
}
