package gak.backend.selection;

import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.application.SelectionService;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.model.Selection;
import gak.backend.member.MemberServiceTest;
import gak.backend.domain.selection.dto.SelectionDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SelectionServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    FormRepository formRepository;
    @Autowired
    FormService formService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionService questionService;
    @Autowired
    SelectionRepository selectionRepository;
    @Autowired
    SelectionService selectionService;


    //테스트 시작하면 레포지토리에 있는 데이터를 다 지움  하나씩 시작할때마다
    @BeforeEach
    public void deleteAll(){
        memberRepository.deleteAll();
        formRepository.deleteAll();
        questionRepository.deleteAll();
        selectionRepository.deleteAll();
    }



    //그냥 하나씩 하기 귀찮으니까 박아놓음
    public SelectionDTO.AllSelectionData SelectionSaveRequest1(){
        return SelectionDTO.AllSelectionData.builder()
                .question(new Question())
                .content("객관식 내용")
                .answer(true)
                .build();
    }

//    public SelectionDTO.SelectionInfoDTO SelectionSaveRequestInfo(){
//        return SelectionDTO.SelectionInfoDTO.builder()
//                .id(1L)
//                .content("객관식 내용")
//                .build();
//    }
//
//    @Test
//    @DisplayName("객관식 생성")
//    public void createSelectionFailByDormantTest() throws Exception{
//        //given
//        SelectionDTO.AllSelectionData allSelectionData=SelectionSaveRequest1();
//        Question que=new Question();
//
//        //when
//        selectionService.createSelection(allSelectionData,que.getId());
//
//        //then
//        Assertions.assertThat(selectionRepository.findAll().size()).isEqualTo(1);
//    }
    @Test
    @DisplayName("객관식 생성")
    public void createSelection() {
        // given
        Question question = new Question();
        String content = "선택지 내용";
        boolean answer = true;

        // when
        SelectionDTO.AllSelectionData selectionData = SelectionDTO.AllSelectionData.builder()
                .question(question)
                .content(content)
                .answer(answer)
                .build();
        Selection selection = selectionData.of(question);

        // then
        Assertions.assertThat(selection.getQuestion()).isEqualTo(question);
        Assertions.assertThat(selection.getContent()).isEqualTo(content);
        Assertions.assertThat(selection.isAnswer()).isEqualTo(answer);

    }







}
