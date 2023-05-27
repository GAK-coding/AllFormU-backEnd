package gak.backend.domain.section.model;

import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Section {
    @Id
    @GeneratedValue
    @Column(name="section_id")
    private Long id;

    private String title;

    private String content;
//
//    @OneToMany(mappedBy = "section")
//    private List<Question> questions;

    private int pageNum;



}
