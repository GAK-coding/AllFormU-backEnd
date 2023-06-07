package gak.backend.domain.member.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static gak.backend.global.constants.JwtConstants.PREFIX_REFRESHTOKEN;
import static gak.backend.global.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME_SECOND;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDaoImpl implements RefreshTokenDao{
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public void createRefreshToken(Long memberId, String refreshToken) {
        stringRedisTemplate.opsForValue().set(PREFIX_REFRESHTOKEN + memberId
                , refreshToken
                , Duration.ofSeconds(REFRESH_TOKEN_EXPIRE_TIME_SECOND));
    }

    @Override
    public String getRefreshToken(Long memberId) {
        return stringRedisTemplate.opsForValue().get(PREFIX_REFRESHTOKEN + memberId);
    }

    @Override
    public void removeRefreshToken(Long memberId) {
        stringRedisTemplate.delete(PREFIX_REFRESHTOKEN + memberId);
    }

    @Override
    public boolean hasKey(Long memberId) {
        return stringRedisTemplate.hasKey(PREFIX_REFRESHTOKEN + memberId);
    }
}
