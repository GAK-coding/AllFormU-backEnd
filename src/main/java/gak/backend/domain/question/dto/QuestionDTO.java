package gak.backend.domain.question.dto;

import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.model.Format;
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

@Getter
@NoArgsConstructor
public class QuestionDTO implements Serializable{

    private Long id;
    private Form form;
    private List<Selection> options = new ArrayList<>();
    private List<Description> descriptions = new ArrayList<>();
    private String title;
    private String content;
    private boolean required;
    private int sectionNum;
    @Enumerated(EnumType.STRING)
    private Format type;


    @Builder
    public QuestionDTO(Long id, Form form, List<Selection> options, List<Description> descriptions, String title, String content, boolean required, int sectionNum, Format type) {
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

}
