package gak.backend.domain.member.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.model.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTime{
    //@JsonBackReference
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String email;

    private int password;

    @OneToMany(mappedBy = "author") //변경 가능성 있음.
    private List<Form> responseForm = new ArrayList<>();


    @OneToMany(mappedBy = "author")
    private List<Form> requestForm = new ArrayList<>();

    @Enumerated
    @Column(name = "role_type")
    private Role role;

    @Enumerated
    @Column(name = "member_status")
    private Status status;

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

}