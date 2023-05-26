package gak.backend.domain.selection.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.exception.NotFoundQuestionException;
import gak.backend.domain.question.model.QQuestion;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.dto.SelectionDTO;
import gak.backend.domain.selection.exception.NotFoundSelectionException;
import gak.backend.domain.selection.model.QSelection;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelectionService {

    @PersistenceContext
    private EntityManager entityManager;
    private final SelectionRepository selectionRepository;
    //selection 생성


//    @Transactional
//    public Selection createInit(FormDTO formDTO){
//        Selection selection=new Selection();
//        for (QuestionDTO question : formDTO.getQuestions()) {
//
//            if(!question.getOptions().isEmpty()) {
//                for(SelectionDTO selectionDTO : question.getOptions()) {
//                    System.out.println("Selection : " + selectionDTO.getContent());
//
//                    selection.setContent(selectionDTO.getContent());
//
//                }
//
//            }
//        }
////        selection.create(selectionDTO.getContent(),selectionDTO.getAnswer());
//        return selectionRepository.save(selection);
//    }
    @Transactional
    public List<Long> createSelection(SelectionDTO selectionDTO,Long QuestionId){
        QQuestion qQuestion=QQuestion.question;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        List<Long> SelectionId=new ArrayList<>();

        Question question_sgl = query
                .selectFrom(qQuestion)
                .where(qQuestion.question.id.eq(QuestionId))
                .fetchOne();


        if(question_sgl==null){
            throw new NotFoundSelectionException(QuestionId);
        }


        List<Selection> sel_List=question_sgl.getOptions();
        sel_List.add(selectionDTO.of(question_sgl));
        List<Selection> saveSelection=selectionRepository.saveAll(sel_List);


        //save이후에 pk값을 받아올 수 있음.
        for(Selection savedSelection : saveSelection) {
            Long id = savedSelection.getId();
            SelectionId.add(id);
            System.out.println("SelectionId:" + id);
        }
        return SelectionId;
    }

    //SelectionId 해당 Selection 조회
    public Selection getSelection(Long id){
        return selectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Description not found with id: " + id));
    }

    //quistion_id로 해당 selection 조회
    public List<Selection> getSelectionByQ(Long questionId) {
        // Selection 테이블에서 questionId로 조회한 결과를 리스트 형태로 반환
        //List<Selection> selections = selectionRepository.findById(questionId);
        return selectionRepository.findByQuestionId(questionId);
    }



    //content update
    @Transactional
    public Selection updateContent(Long QuestionId, Long SelectionId, String newContent){

        QSelection qSelection=QSelection.selection;
        QQuestion qQuestion=QQuestion.question;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        Selection selection_sgl = query
                .selectFrom(qSelection)
                .where(qSelection.id.eq(SelectionId)
                        .and(qQuestion.id.eq(QuestionId)))
                .fetchOne();

        if(selection_sgl==null){
            throw new NotFoundSelectionException(QuestionId,SelectionId);
        }

        selection_sgl.updateContent(newContent);
        return selection_sgl;
    }

    //answer update
    @Transactional
    public Selection updateAnswer(Long id, Boolean newAnswer){
        Selection description=getSelection(id);
        description.updateAnswer(newAnswer);
        return selectionRepository.save(description);
    }

    @Transactional
    public void deleteSelection(Long id){

        Optional<Selection> optionalValue = selectionRepository.findById(id);
        if (!optionalValue.isPresent()) {
            throw new RuntimeException("Form is not present");
        }
        selectionRepository.deleteById(id);

    }


}
