package gak.backend.domain.description.model;

import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Description extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "description_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quesion_id")
    private Question question;
    private String content;
    private String answer;
    private boolean quiz;
}
