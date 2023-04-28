package gak.backend.domain.option.dao;

import gak.backend.domain.option.model.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Selection, Long> {
}
