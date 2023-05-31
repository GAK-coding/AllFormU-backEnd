package gak.backend.domain.question.dto;

import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.model.Format;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.dto.SelectionDTO;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gak.backend.domain.question.model.Format.Description_SHORT;
import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;
@Getter
@NoArgsConstructor
public class QuestionDTO implements Serializable{

    private Long id;
    private Form form;
    private List<SelectionDTO.AllSelectionData> options =new ArrayList<>();
    private List<DescriptionDTO> descriptions =new ArrayList<>();
    private String title;
    private String content;
    private Boolean required;
    private boolean quiz;
    private Integer sectionNum;

    @Enumerated(EnumType.STRING)
    private Format type;


    @Builder
    public QuestionDTO(Long id, Form form, List<SelectionDTO.AllSelectionData> options, List<DescriptionDTO> descriptions, String title, String content, Boolean required, boolean quiz, Integer sectionNum, Format type) {
        this.id = id;
        this.form = form;
        this.options = options;
        this.descriptions = descriptions;
        this.title = title;
        this.content = content;
        this.required = required;
        this.quiz = quiz;
        this.sectionNum = sectionNum;
        this.type = type;
    }


    //첫 생성 시에는 (create) 타입형식만 받아오기 때문에 다른 값들은 뭐가 들어가도 상관 x
    //보여지는 것은 title이고 title은 실제 값 저장 되야하고
    //추가로 sectionNum은 따로 받아야 함.
    public Question of (Form form){
        return Question.builder()
                .form(form)
                .title(title)
                .content(content)
                .required((required!=null)?required:false)
                .quiz(quiz)
                .sectionNum((sectionNum!=null)?sectionNum:0)
                .type((type!=null)?type:Description_SHORT)
                .build();
    }
    public List<Selection> toSelection(SelectionRepository selectionRepository,Question question) {
        List<Selection> selectionList = new ArrayList<>();
        if(options.size()<1){
            Selection selection = Selection.builder()
                    .question(question)
                    .build();
            selectionList.add(selection);
            selectionRepository.saveAll(selectionList);
        }

        for (SelectionDTO.AllSelectionData allSelectionData : options) {
            Selection selection = Selection.builder()
                    .content((allSelectionData.getContent() !="")? allSelectionData.getContent():"입력 값 없음")
                    .question(question)
                    .build();
            selectionList.add(selection);
            selectionRepository.saveAll(selectionList);
        }
        return selectionList;
    }

    public List<Description> toDescription(DescriptionRepository descriptionRepository,Question question) {
        List<Description> descriptionList = new ArrayList<>();
        if(descriptions.size()<1){
            Description description= Description.builder()
                    .question(question)
                    .build();
            descriptionList.add(description);
            descriptionRepository.saveAll(descriptionList);
        }
        for (DescriptionDTO descriptionDTO : descriptions) {
            Description description = Description.builder()
                    .content((descriptionDTO.getContent()!="")? descriptionDTO.getContent():"입력 값 없음")
                    .question(question)
                    .build();
            descriptionList.add(description);
            descriptionRepository.saveAll(descriptionList);
        }
        return descriptionList;
    }



}
