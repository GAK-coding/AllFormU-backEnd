package gak.backend.domain.form.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.model.Question;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Form extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "form_id")
    private Long id;

    public void setAuthor(Member author) {
        this.author = author;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "form",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private Separator separator;

    @Enumerated(EnumType.STRING)
    @Column(name = "Correspond_status")
    private Correspond correspond;
    private String title;
    private String FormImage;
    private String content;
    private List<String> timeout; //시작시간, 마감시간 추가
    private int responsor_count; //응답자 수 추가

    //private boolean required;

    private boolean fix; // 수정가능 : 0 수정 불가능 : 1

    @Builder
    public Form(String title, Boolean fix, String content,String FormImage){
        this.title=title;
        this.fix=fix;
        this.content=content;
        this.FormImage=FormImage;
    }

    public FormDTO.PagingDataDTO toPagingData(){
        return FormDTO.PagingDataDTO.builder()
                .id(this.id)
                .content(this.content)
                .title(this.title)
                .timeout(this.timeout)
                .responsor(this.responsor_count)
                .build();
    }


    public void AuthorSetting(Member author){
        this.author=author;
    }

    public void UpdateSelectForm(FormDTO.UpdateFormData updateFormData){

        this.content=(updateFormData.getContent()!=null) ? updateFormData.getContent() : this.content;
        this.fix=(updateFormData.getFix()!=null) ? updateFormData.getFix() : this.fix;
        this.title=(updateFormData.getTitle()!=null) ? updateFormData.getTitle() : this.title;
        this.FormImage=(updateFormData.getFormImage()!=null) ? updateFormData.getFormImage() : this.FormImage;

    }


    public void QuestionSetting(List<Question> questions) {
        this.questions = questions;
    }
    public void ResponsorCntSetting(int responsor_count){
        this.responsor_count=responsor_count;
    }
    public boolean FixSetting(boolean fix){
        this.fix=fix;
        return fix;
    }
    public void SeparatorSetting(Separator separator){
        this.separator=separator;
    }
    public void TimeoutSetting(List<String> timeout){
        this.timeout=timeout;
    }
    public void CorrespondSetting(Correspond correspond){
        this.correspond=correspond;
    }

    // =========변경 가능-----------
//    @ManyToOne
//    @JoinColumn(name = "form_id")
//    private Form parent;
//    @OneToMany(mappedBy = "parent") //cascade = CascadeType.ALL, orphanRemoval = true) 변경 가능
//    private List<Form> child = new ArrayList<>();
    //==========================

}
