package gak.backend.domain.member.model;

import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.model.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String email;

    private String password;
    private String image;

//    @OneToMany(mappedBy = "author") //변경 가능성 있음.
//    private List<Form> responseForm = new ArrayList<>();
//
//
//    @OneToMany(mappedBy = "author")
//    private List<Form> requestForm = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private Status status;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;


    public void UpdateMemberNickname(String newNickname){ this.nickname = newNickname; }
    public void UpdateMemberImage(String newImage){ this.image = newImage; }

    public void UpdateMemberPassword(String newPwd){ this.password = newPwd; }
    public void UpdateMemberStatus(Status newStatus){ this.status = newStatus;}
    public void UpdateMemberRole(Role newRole){this.role = newRole;}

}
