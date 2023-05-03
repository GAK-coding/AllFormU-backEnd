package gak.backend.domain.selection.dao;

import gak.backend.domain.selection.model.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long> {

    List<Selection> findByQuestionId(Long question_id);
}
