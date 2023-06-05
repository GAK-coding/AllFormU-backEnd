package gak.backend.domain.form.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.QForm;
import gak.backend.domain.member.model.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

   Optional<Form> findById(Long id);


}
