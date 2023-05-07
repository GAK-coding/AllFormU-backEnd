package gak.backend.domain.question.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.model.Form;
//import gak.backend.domain.grid.model.Grid;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
//TODO img 추가해야함.

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;



    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;



    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Selection> options = new ArrayList<>();



    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Description> descriptions = new ArrayList<>();

//    @OneToMany(mappedBy="question")
//    private List<Grid> grids = new ArrayList<>();

    private String title;
    private String content;
    private boolean required;
    private int sectionNum;


    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private Format type;

    public void setContent(String content){
        this.content=content;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public void OptionsSetting(List<Selection> options) {
        this.options = options;
    }
    public void DescriptionsSetting(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public void setForm(Form form) {
        this.form = form;
    }
    public void setType(Format type) {
        this.type = type;
    }

    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }

}