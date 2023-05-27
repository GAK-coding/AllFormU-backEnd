//package gak.backend.global.jwt;
//
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//
//
////JWT Custom Filter를 만들기 위한 클래스
//public class JwtFilter extends GenericFilterBean{
////
////    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
////    public static final String AUTHORIZATION_HEADER = "Authorization";
//////    private TokenProvider tokenProvider;
//////    public JwtFilter(TokenProvider tokenProvider){
//////        this.tokenProvider = tokenProvider;
//////    }
////
////    //실제 필터링 로직이 들어감.
////    @Override
////    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
////            throws IOException, ServletException, java.io.IOException {
////        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
////        String jwt = resolveToken(httpServletRequest);
////        String reqeustURI = httpServletRequest.getRequestURI();
////
//////        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//////            Authentication authentication = tokenProvider.getAuthentication(jwt);
//////            SecurityContextHolder.getContext().setAuthentication(authentication);
//////            logger.debug("Security Context에 '{} 인증 정보를 저장합니다, uri: {}", authentication.getName(), reqeustURI);
//////        } else{
//////            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", reqeustURI);
//////        }
//////        filterChain.doFilter(servletRequest, servletResponse);
//////    }
////
////    //Request Header에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드 추가
////    //resolveToken을 통해 토큰을 받아와서 유효성 검증, 정상이면 SecurityContext에 저장.
////    private String resolveToken(HttpServletRequest request){
////        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
////        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
////            return bearerToken.substring(7);
////        }
////        return null;
////    }
//}
