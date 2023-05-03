package gak.backend.domain.description.dao;

import gak.backend.domain.description.model.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {
    List<Description> findByQuestionId(Long question_id);
}
