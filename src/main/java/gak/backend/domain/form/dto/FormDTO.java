package gak.backend.domain.form.dto;

import gak.backend.domain.form.model.Correspond;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.Separator;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import gak.backend.domain.question.dto.QuestionDTO;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class FormDTO implements Serializable {




    private Long id;
    private Long authorId;
    private List<QuestionDTO> questions;
    private String title;
    private String content;
    private String FormImage;

    //private List<String> timeout;
    //private List<Integer> Timeout;
//    @Enumerated(EnumType.STRING)
//    private Separator separator;
//    @Enumerated(EnumType.STRING)
//    private Correspond correspond;

    //private boolean required;
    private Boolean fix;


//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class CreateForm{
//
//        private String title;
//        private String content;
//        private Boolean fix;
//        public Form of (){
//            return Form.builder()
//                    .title(title)
//                    .fix((fix!=null)?fix:false)
//                    .content(content)
//                    .build();
//        }
//    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateFormData{
        private String content;
        private Boolean fix;
        private String title;
        private String FormImage;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSelectForm{

        private List<String> SectionName;
        private Form form;


    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagingDataDTO{
        private Long id;
        private String content;
        private String title;

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagingDTO{
        private boolean nextPage;
        List<PagingDataDTO> PagingData=new ArrayList<>();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllFormData{


        private Long id;
        private Boolean fix;
        private Long authorId;
        private List<QuestionDTO> questions;
        private String title;
        private String content;
        private String FormImage;
        private List<String> timeout;

        public Form of (){
            return Form.builder()
                    .title(title)
                    .fix((fix!=null)?fix:false)
                    .content(content)
                    .FormImage(FormImage)
                    .build();
        }
        public List<Question> toQuestions(Form form) {

            List<Question> questionList = new ArrayList<>();
            for (QuestionDTO questionDTO : questions) {
                Question question = Question.builder()
                        .form(form)
                        .title(questionDTO.getTitle())
                        .required(questionDTO.getRequired())
                        .sectionNum(questionDTO.getSectionNum())
                        .type(questionDTO.getType())
                        .build();
                questionList.add(question);
            }
            return questionList;
        }

        //this.timeout=timeout;


    }

//    @Builder
//    public FormDTO(Long id, Long authorId, List<QuestionDTO> questions,  String title, String content,Boolean fix){
//
//        this.id=id;
//        this.fix=fix;
//        this.authorId=authorId;
//        this.questions=questions;
//        this.title=title;
//        this.content=content;
//        //this.timeout=timeout;
//
//
//    }

//    public Form of (){
//        return Form.builder()
//                .title(title)
//                .fix((fix!=null)?fix:false)
//                .content(content)
//                .build();
//    }
//    public List<Question> toQuestions(Form form) {
//
//        List<Question> questionList = new ArrayList<>();
//        for (QuestionDTO questionDTO : questions) {
//            Question question = Question.builder()
//                    .form(form)
//                    .title(questionDTO.getTitle())
//                    .required(questionDTO.getRequired())
//                    .sectionNum(questionDTO.getSectionNum())
//                    .type(questionDTO.getType())
//                    .build();
//            questionList.add(question);
//        }
//        return questionList;
//    }



}

