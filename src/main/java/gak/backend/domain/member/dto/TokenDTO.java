package gak.backend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//클라이언트에게 토큰을 보내기 위한 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String grantType; //JWT 인증 타입(Bearer 사용) -> HTTP헤어에 prefix로 붙여주는 타입이기도 함.
    private String accessToken;
    private String refreshToken;
}
