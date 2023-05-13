package gak.backend.domain.member.model;

import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.model.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static gak.backend.domain.member.dto.MemberDTO.*;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTime{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String email;

    private String password;

    @OneToMany(mappedBy = "author") //변경 가능성 있음.
    private List<Form> responseForm = new ArrayList<>();


    @OneToMany(mappedBy = "author")
    private List<Form> requestForm = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private Status status;

    @Builder
    public Member(Long id, String nickname, String email, String password, Role role, Status status){
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public UpdatePasswordRequest toUpdatePasswordRequest(String newPwd){
        return UpdatePasswordRequest.builder()
                .id(this.id)
                .password(this.password)
                .newPwd(newPwd)
                .build();
    }
    public UpdateNicknameDTO toUpdateNicknameDTO(){
        return UpdateNicknameDTO.builder()
                .id(this.id)
                .nickname(this.nickname)
                .build();
    }
    public UpdatePasswordDTO toUpdatePasswordDTO(){
        return UpdatePasswordDTO.builder()
                .id(this.id)
                .password(this.password)
                .build();
    }

    public MemberStatusInfoDTO toMemberStatusInfoDTO(){
        return MemberStatusInfoDTO.builder()
                .id(this.id)
                .email(this.email)
                .status(this.status)
                .build();
    }

    public MemberInfoDTO toMemberInfoDTO(){
        return MemberInfoDTO.builder()
                .id(this.id)
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .status(this.status)
                .modifiedTime(super.getModifiedDate())
                .createdTime(super.getCreatedDate())
                .build();
    }

    public void UpdateMemberNickname(String newNickname){ this.nickname = newNickname; }

    public void UpdateMemberPassword(String newPwd){ this.password = newPwd; }
    public void UpdateMemberStatus(Status newStatus){ this.status = newStatus;}




}