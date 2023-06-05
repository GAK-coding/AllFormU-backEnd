package gak.backend.selection;

import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.selection.application.SelectionService;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.member.MemberServiceTest;
import gak.backend.domain.selection.dto.SelectionDTO;
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
                .content("객관식 내용")
                .answer(true)
                .build();
    }

    @Test
    @DisplayName("객관식 생성")
    public void createSelectionFailByDormantTest() throws Exception{

    }







}
