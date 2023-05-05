package gak.backend.domain.member.dto;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Role;
import gak.backend.domain.member.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberDTO {
    @Getter
    @Builder //이거 여기는 없어도 되지 않나?
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSaveRequest{
        private String nickname;
        private String email;
        private String password;
        //private Role role; //생성자인지 응답자인지 받음.


        public Member toEntity(){
            return Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(password)
                    .role(Role.Role_Responsor)
                    .status(Status.STATUS_MEMBER) //멤버가 생성된다는 것은 회원가입 -> 이것은 멤버 상태로 한다는 뜻
                    .build();

        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoDTO{
        private Long id;
        private String nickname;
        private String email;
        private String password;
        private Role role;
        private Status status;
        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;

    }
}
