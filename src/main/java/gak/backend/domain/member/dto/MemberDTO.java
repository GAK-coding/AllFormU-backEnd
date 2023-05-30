package gak.backend.domain.member.dto;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Role;
import gak.backend.domain.member.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class MemberDTO {
    @Getter
    //@Builder
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
                    .nickname(nickname)
                    .role(Role.Role_Responsor)
                    .status(Status.STATUS_MEMBER) //멤버가 생성된다는 것은 회원가입 -> 이것은 멤버 상태로 한다는 뜻
                    .build();

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginReqeust{
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailReqest{
        private String email;
        private int num; //0,1 추후 더 길어질 수 있음 우선은 회원가입, 비밀번호 재설정 밖에 없음.
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNicknameRequest{
        private Long id;
        private String newNickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePasswordRequest{
        private Long id;
        private String password;
        private String newPwd;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNicknameDTO{
        private Long id;
        private String nickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePasswordDTO{
        private Long id;
        private String password;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailDTO{
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberStatusInfoDTO{
        private long id;
        private String email;
        private Status status;
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

//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class MemberResponseDTO{
//        private HttpStatus httpStatus;
//        private String message;
//    }

}
