package gak.backend.domain.selection.api;

import gak.backend.domain.selection.application.SelectionService;
import gak.backend.domain.selection.dto.SelectionDTO;
import gak.backend.domain.selection.model.Selection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class SelectionController {
    private final SelectionService selectionService;

    //Selection 생성
    @PostMapping("/selection/createSelection")
    public String add(@RequestBody SelectionDTO selectionDTO){
        selectionService.createSelection(selectionDTO);
        return "create selection";
    }

    //Selection 조회
    @GetMapping("/selection/getSelection/{id}")
    public Selection getId(@PathVariable("id")Long id){
        return selectionService.getSelection(id);
    }

    //Question_id로 해당 질문에 객관식 전부 조회
    @GetMapping("/selection/getSelectionByQ/{question_id}")
    public List<Selection> getIdByQ(@PathVariable("question_id")Long question_id){
        //List <Selection> selectionList=selectionService.getOptionByQ(question_id);
        return selectionService.getSelectionByQ(question_id);
    }

    //객관식 수정
    @PutMapping("/selection/updateContent/{id}")
    public ResponseEntity<Selection> updateSelectionContent(@PathVariable(value="id")Long id,
                                                            @RequestBody Map<String, String> requestBody){
        String updateContent=requestBody.get("content");
        final Selection updatedSelection=selectionService.updateContent(id,updateContent);
        return ResponseEntity.ok(updatedSelection);
    }

    //객관식 정답 수정
    @PutMapping("/selection/updateAnswer/{id}")
    public ResponseEntity<Selection> updateSelectionAnswer(@PathVariable(value="id")Long id,
                                                            @RequestBody Map<String, String> requestBody){
        Boolean updateAnswer= Boolean.valueOf(requestBody.get("answer"));
        final Selection updatedSelection=selectionService.updateAnswer(id,updateAnswer);
        return ResponseEntity.ok(updatedSelection);
    }

    //객관식 하나 삭제
    @DeleteMapping("/selection/deleteSelection/{id}")
    public String deleteSelection(@PathVariable("id")Long id){
        selectionService.deleteSelection(id);
        return "selection delete";
    }


}
