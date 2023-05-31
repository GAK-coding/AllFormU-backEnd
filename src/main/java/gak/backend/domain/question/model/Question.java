package gak.backend.domain.question.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.model.Form;
//import gak.backend.domain.grid.model.Grid;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private String sectionName; //sectionName추가
    private boolean quiz;


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



    //null체크를 해서 값이 넘어오지 않은 필드에 대해서는 원래의 값을 유지하기 위함.
    //null값을 받지 않을 시에는 체크를 하기가 힘듦
    //DTO에서 boolean으로 설정 시 아무 값도 입력받지 않았을 때 기본 값 false로 넘어옴
    //즉 실제로 true를 false로 바꾸고 싶어서 false를 넘겨줬는데
    //그 값이 아무 값도 전달되지 않은 false인지 의도적인 false인지 구별하기 힘듦
    //필드가 많을 시에 해당 코드는 매우 비효율적이라 좀 생각을 해보는게 좋을듯
    public void UpdateSelectQuestion(QuestionDTO questionDTO){
        this.title=(questionDTO.getTitle() !=null)? questionDTO.getTitle():this.title;
        this.required=(questionDTO.getRequired() !=null)? questionDTO.getRequired():this.required;
        this.sectionNum=(questionDTO.getSectionNum() !=null)? questionDTO.getSectionNum():this.sectionNum;
        this.type=(questionDTO.getType() !=null)? questionDTO.getType():this.type;

    }
    public void setForm(Form form) {
        this.form = form;
    }
    public void setType(Format type) {
        this.type = type;
    }

//    public void setSectionNum(int sectionNum) {
//        this.sectionNum = sectionNum;
//    }
//    public void setRequired(boolean required) {
//        this.required = required;
//    }

    @Builder
    public Question(Long id,Form form, String title, String content,boolean required, int sectionNum, String sectionName, boolean quiz, Format type){
        this.form = form;
        this.title = title;
        this.content = content;
        this.required = required;
        this.sectionNum = sectionNum;
        this.sectionName= sectionName;
        this.quiz = quiz;
        this.type = type;
    }

}