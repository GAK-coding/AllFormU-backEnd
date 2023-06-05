//package gak.backend.global.jwt;
//
//import com.fasterxml.jackson.databind.ser.Serializers;
//import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
//import gak.backend.domain.member.dto.TokenDTO;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import lombok.Value;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Collection;
//import java.util.Date;
//import java.util.stream.Collectors;
//
////Token의 생성, 인증정보 조회, 유효성 검증, 암호화 설정 등의 역할을 수행하는 class
//@Component
//@Slf4j
//public class TokenProvider implements InitializingBean {
//    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
//    private static final String AUTHORITIES_KEY = "NeighborAPI";
//    private final String secret;
//    private final long tokenValidityInMilliseconds;
//    private Key key;
//
//    public TokenProvider(
//            @Value("${jwt.secret}") String secret,
//            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds) {
//        this.secret = secret;
//        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(secret);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    //token create algorithm
//    public TokenDTO createToken(Authentication authentication) {
//        //권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        long now = (new Date()).getTime();
//        //accesstoken 생성
//        Date validity = new Date(now + this.tokenValidityInMilliseconds);
//        String accessToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim(AUTHORITIES_KEY, authorities)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .setExpiration(validity)
//                .compact();
//
//        //Refresh Token 생성
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now + this.))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//
//        return TokenDTO.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//
//    //인증 정보 조회
//    public Authentication getAuthentication(String accesstoken) {
//        Claims claims = Jwts
//                .parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(accesstoken)
//                .getBody();
//
//        //클레임(pay에서 권한 정보 가져오기
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        //UserDetails 객체를 만들어서 Authentication 반환
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//
//    //token의 유효성 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            logger.info("잘못된 JWT 서명입니다.");
//        } catch (ExpiredJwtException e) {
//            logger.info("만료된 JWT 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//            logger.info("지원되지 않는 JWT 토큰입니다.");
//        } catch (IllegalArgumentException e) {
//            logger.info("JWT 토큰이 잘못되었습니다.");
//        }
//        return false;
//    }
//
//
//}
