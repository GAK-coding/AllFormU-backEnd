package gak.backend.domain.grid.model;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor
public class Grid {
    @Id
    @GeneratedValue
    @Column(name="grid_id")
    private Long id;



}
