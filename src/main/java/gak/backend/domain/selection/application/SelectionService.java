package gak.backend.domain.selection.application;

import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.dto.SelectionDTO;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelectionService {
    private final SelectionRepository selectionRepository;
    //selection 생성


//    @Transactional
//    public Selection createInit(FormDTO formDTO){
//        Selection selection=new Selection();
//        for (QuestionDTO question : formDTO.getQuestions()) {
//            // 질문의 첫번째 설명을 저장
//            if(!question.getOptions().isEmpty()) {
//                for(SelectionDTO selectionDTO : question.getOptions()) {
//                    System.out.println("Selection : " + selectionDTO.getContent());
//
//                    selection.setContent(selectionDTO.getContent());
//                    // 저장할 작업 수행
//                }
//
//            }
//        }
////        selection.create(selectionDTO.getContent(),selectionDTO.getAnswer());
//        return selectionRepository.save(selection);
//    }
    @Transactional
    public Selection createSelection(SelectionDTO selectionDTO){
        Selection selection=new Selection();
        selection.create(selectionDTO.getContent(),selectionDTO.getAnswer());
        return selectionRepository.save(selection);
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
    public Selection updateContent(Long id, String newContent){
        Selection description=getSelection(id);
        description.updateContent(newContent);
        return selectionRepository.save(description);
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
