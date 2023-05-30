package gak.backend.domain.grid.dao;

import gak.backend.domain.grid.model.Grid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GridRepository extends JpaRepository<Grid, Long> {

}
