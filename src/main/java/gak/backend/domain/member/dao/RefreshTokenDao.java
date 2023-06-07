package gak.backend.domain.member.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao {
    void createRefreshToken(Long memberId, String refreshToken);
    String getRefreshToken(Long memberId);
    void removeRefreshToken(Long memberId);
    boolean hasKey(Long memberId);
}
