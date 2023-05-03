package gak.backend.domain.section.dao;


import gak.backend.domain.section.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    //test codes 이야 ㅇㅇㅇㅇ
}
