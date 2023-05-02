package gak.backend.domain.selection.application;

import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.dto.SelectionDTO;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelectionService {
    private final SelectionRepository selectionRepository;
    //selection 생성
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

    //quistion_id로 해당 option 조회
    public Optional<Selection> getOptionByQ(Long quesion_id){
        return selectionRepository.findById(quesion_id);

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
