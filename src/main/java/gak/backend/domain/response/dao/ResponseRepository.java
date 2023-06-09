package gak.backend.domain.response.dao;

import gak.backend.domain.description.model.Description;
import gak.backend.domain.response.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    Optional<Response> findById(Long id);

    int countResponseByQuestionId(Long question_id);

    boolean existsByResponsorId(Long responsor_id);

    boolean existsByResponsorIdAndQuestionId(Long responsorId, Long questionId);
    Optional<Description> findResponseByResponsorIdAndQuestionId(Long responsorId, Long questionId);

    List<Response> findByResponsorIdAndQuestionId(Long responsorId, Long QuestionId);

    //통계를 위해 사용
    List<Response> findByQuestionId(Long question_id);
}
