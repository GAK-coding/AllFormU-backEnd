package gak.backend.domain.selection.application;

import com.querydsl.jpa.JPAExpressions;
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

import static gak.backend.domain.selection.model.QSelection.selection;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelectionService {

    @PersistenceContext
    private EntityManager entityManager;
    private final SelectionRepository selectionRepository;


    @Transactional
    public List<SelectionDTO.SelectionInfoDTO> createSelection(SelectionDTO.AllSelectionData allSelectionData, Long QuestionId){
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


        //selection 객체는 처음에 질문 생성 시 default로 만들어 짐
        //해당 selection 객체를 지우고 아래 코드를 실행해도 객체를 인식하고 값이 저장 됨.
        List<Selection> sel_List=question_sgl.getOptions();
        sel_List.add(allSelectionData.of(question_sgl));
        List<Selection> saveSelection=selectionRepository.saveAll(sel_List);

        List<SelectionDTO.SelectionInfoDTO> ListSelectionInfo=new ArrayList<>();
        //save이후에 pk값을 받아올 수 있음.
        for(Selection savedSelection : saveSelection) {

            SelectionDTO.SelectionInfoDTO temp_data=savedSelection.SelectionInfoDTO();
            ListSelectionInfo.add(temp_data);

        }

        return ListSelectionInfo;
    }

    @Transactional
    public List<SelectionDTO.SelectionInfoDTO> createLinear(SelectionDTO.LinearInfoDTO linearInfoDTO, Long QuestionId){

        QQuestion qQuestion=QQuestion.question;
        QSelection qSelection= selection;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        Question question_sgl = query
                .selectFrom(qQuestion)
                .where(qQuestion.question.id.eq(QuestionId))
                .fetchOne();


        if(question_sgl==null){
            throw new NotFoundSelectionException(QuestionId);
        }


        //selection 객체가 있으면 다 지우고 다시 생성해야 하기 때문
        if(question_sgl.getOptions()!=null){
                query.delete(qSelection)
                        .where(qSelection.question.eq(
                                JPAExpressions.selectFrom(qQuestion)
                                        .where(qQuestion.id.eq(QuestionId))
                        ))
                        .execute();
        }



        List<SelectionDTO.SelectionInfoDTO> ListSelectionInfo=new ArrayList<>();

        for(String linear : linearInfoDTO.getContent()){

                //객체 생성하고 하나하나 저장
                Selection temp_selection=new Selection();
                temp_selection.createLinear(question_sgl,linear);
                selectionRepository.save(temp_selection);

                //pk와 content 내용 저장
                SelectionDTO.SelectionInfoDTO temp_data=temp_selection.SelectionInfoDTO();
                ListSelectionInfo.add(temp_data);

        }

        return ListSelectionInfo;
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

        QSelection qSelection= selection;
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
