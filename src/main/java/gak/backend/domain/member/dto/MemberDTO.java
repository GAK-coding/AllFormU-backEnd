package gak.backend.domain.member.dto;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Role;
import gak.backend.domain.member.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

public class MemberDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSaveRequest{
        private String nickname;
        private String email;
        private String password;
        private String image;
        private Role role; //생성자인지 응답자인지 관리자인지 받음.

        @Bean
        public void passwordEncryption(PasswordEncoder passwordEncoder){
            this.password = passwordEncoder.encode(password);
        }
        public Member toEntity(){
            return Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(password)
                    .image(image)
                    .nickname(nickname)
                    //.role(Role.Role_Responsor)
                    .status(Status.STATUS_MEMBER) //멤버가 생성된다는 것은 회원가입 -> 이것은 멤버 상태로 한다는 뜻
                    .build();

        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginReqeust{
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication(){
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailReqest{
        private String email;
        private int num; //0,1 추후 더 길어질 수 있음 우선은 회원가입, 비밀번호 재설정 밖에 없음.
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNicknameRequest{
        private Long id;
        private String newNickname;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateImageRequest{
        private Long id;
        private String newImage;
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
    public static class UpdateImageDTO{
        private Long id;
        private String image;
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
    @Builder
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
        private String image;
        private Role role;
        private Status status;

        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoTokenDTO{
        private Long id;
        private String nickname;
        private String email;
        private String password;
        private String image;
        private Role role;
        private Status status;
        TokenDTO tokenDTO;

        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;

    }

    //JWT 관련 추가
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OperatorDTO{
        private Long memberId;
        private String email;
        private String nickname;
        private String password;
        private String isUse;
        private Set<AuthDTO> authorities;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthDTO{
        private Long memberId;
        private String nickname;
    }

    //구글 요청
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaveMemberGoogleRequest{
        private String email;
        private String nickname;
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
