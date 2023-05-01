package gak.backend.domain.form.dao;

import gak.backend.domain.form.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    //들어갈거
}
