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

import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;
@Getter
@NoArgsConstructor
public class QuestionDTO implements Serializable{

    private Long id;
    private Form form;
    private List<SelectionDTO> options = new ArrayList<>();
    private List<DescriptionDTO> descriptions = new ArrayList<>();
    private String title;
    private String content;
    private boolean required;
    private int sectionNum;

    @Enumerated(EnumType.STRING)
    private Format type;


    @Builder
    public QuestionDTO(Long id, Form form, List<SelectionDTO> options, List<DescriptionDTO> descriptions, String title, String content, boolean required, int sectionNum, Format type) {
        this.id = id;
        this.form = form;
        this.options = options;
        this.descriptions = descriptions;
        this.title = title;
        this.content = content;
        this.required = required;
        this.sectionNum = sectionNum;
        this.type = type;
    }

    public Question of (Form form){
      return Question.builder()
              .form(form)
              .title(title)
              .content(content)
              .required(required)
              .sectionNum(sectionNum)
              .type(type)
              .build();
   }
    public List<Selection> toSelection(SelectionRepository selectionRepository,Question question) {
        List<Selection> selectionList = new ArrayList<>();
        for (SelectionDTO selectionDTO : options) {
            Selection selection = Selection.builder()
                    .content(selectionDTO.getContent())
                    .question(question)
                    .build();
            selectionList.add(selection);
            selectionRepository.saveAll(selectionList);
        }
        return selectionList;
    }
    public List<Description> toDescription(DescriptionRepository descriptionRepository,Question question) {
        List<Description> descriptionList = new ArrayList<>();
        for (DescriptionDTO descriptionDTO : descriptions) {
            Description description = Description.builder()
                    .content(descriptionDTO.getContent())
                    .question(question)
                    .build();
            descriptionList.add(description);
            descriptionRepository.saveAll(descriptionList);
        }
        return descriptionList;
    }



}
