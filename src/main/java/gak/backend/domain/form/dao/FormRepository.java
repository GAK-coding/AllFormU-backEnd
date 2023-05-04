package gak.backend.domain.form.dao;

import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

   Optional<Form> findById(Long id);
   //List<Form> findByAuthorId(Long id);
}
