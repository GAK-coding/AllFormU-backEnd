package gak.backend.domain.member.model;

import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.model.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static gak.backend.domain.member.dto.MemberDTO.*;

@Getter
@Entity
@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
public class Member extends MemberBase{

    private String provider; //oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    private String providerId; //oauth2를 이용할 경우 아이디값

    @OneToMany(mappedBy = "author") //변경 가능성 있음.
    private List<Form> responseForm = new ArrayList<>();


    @OneToMany(mappedBy = "author")
    private List<Form> requestForm = new ArrayList<>();

    @Builder
    public Member(Long id, String nickname, String email, String password, String image, Status status, LoginType loginType){
        super(id, nickname, email, password, image, Role.Role_Responsor, status, loginType);
    }

    @Builder
    public Member(Long id, String nickname, String email, String password, String image, Status status, LoginType loginType, String provider, String providerId){
        super(id, nickname, email, password, image, Role.Role_Responsor, status, loginType);
        this.provider = provider;
        this.providerId = providerId;
    }


//    @Builder
//    public Member(Long id, String nickname, String email, String password, String image, Role role, Status status){
//        this.id = id;
//        this.nickname = nickname;
//        this.email = email;
//        this.password = password;
//        this.image=image;
//        this.role = role;
//        this.status = status;
//    }

    public UpdatePasswordRequest toUpdatePasswordRequest(String newPwd){
        return UpdatePasswordRequest.builder()
                .id(this.getId())
                .password(this.getPassword())
                .newPwd(newPwd)
                .build();
    }
    public UpdateNicknameDTO toUpdateNicknameDTO(){
        return UpdateNicknameDTO.builder()
                .id(this.getId())
                .nickname(this.getNickname())
                .build();
    }
    public UpdatePasswordDTO toUpdatePasswordDTO(){
        return UpdatePasswordDTO.builder()
                .id(this.getId())
                .password(this.getPassword())
                .build();
    }
    public UpdateImageDTO toUpdateImageDTO(){
        return UpdateImageDTO.builder()
                .id(this.getId())
                .image(this.getImage())
                .build();
    }

    public MemberStatusInfoDTO toMemberStatusInfoDTO(){
        return MemberStatusInfoDTO.builder()
                .id(this.getId())
                .email(this.getEmail())
                .status(this.getStatus())
                .build();
    }

    public MemberInfoDTO toMemberInfoDTO(){
        return MemberInfoDTO.builder()
                .id(this.getId())
                .nickname(this.getNickname())
                .email(this.getEmail())
                .password(this.getPassword())
                .image(this.getImage())
                .role(this.getRole())
                .status(this.getStatus())
                .modifiedTime(super.getModifiedDate())
                .createdTime(super.getCreatedDate())
                .build();
    }

    public OperatorDTO toOperatorDTO(String isUse, Set<AuthDTO> authorities){
        return OperatorDTO.builder()
                .memberId(this.getId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .password(this.getPassword())
                .isUse(isUse)
                .authorities(authorities)
                .build();

    }

//    public void UpdateMemberNickname(String newNickname){ this.nickname = newNickname; }
//    public void UpdateMemberImage(String newImage){ this.image = newImage; }
//
//    public void UpdateMemberPassword(String newPwd){ this.password = newPwd; }
//    public void UpdateMemberStatus(Status newStatus){ this.status = newStatus;}
//    public void UpdateMemberRole(Role newRole){this.role = newRole;}


}