package gak.backend.domain.option.model;

import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Selection extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
    private boolean response;
    private boolean answer;
    private boolean quiz;
}
