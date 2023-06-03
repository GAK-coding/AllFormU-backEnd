package gak.backend.domain.description.dao;

import gak.backend.domain.description.model.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {
    List<Description> findByQuestionId(Long question_id);

    boolean existsByMemberIdAndQuestionId(Long memberId, Long questionId);

    //멤버아이디와 문제 아이디로 자기가 쓴 정답 확인
    Optional<Description> findByMemberIdAndQuestionId(Long memberId, Long questionId);


    int countDescriptionByQuestionId(Long questionId);

}
