package gak.backend.domain.grid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grid extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "Grid_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quesion_id")
    private Question question;
    private String colOption;
    private String rowOption;
    private String answer;



    public void create(String answer,String colOption, String rowOption){
        this.answer=answer;
        this.colOption=colOption;
        this.rowOption=rowOption;
    }
    public void update(String answer){
        this.answer=answer;
    }

    public void setColOption(String colOption) {
        this.colOption = colOption;
    }

    public void setRowOption(String rowOption) {this.rowOption = rowOption;}



}
