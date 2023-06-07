//package gak.backend.domain.oauth.handler;
//
//import gak.backend.domain.member.dao.RefreshTokenDao;
//import gak.backend.domain.member.dto.TokenDTO;
//import gak.backend.domain.oauth.dao.OAuth2AuthorizationRequestBasedOnCookieRepository;
//import gak.backend.global.jwt.TokenProvider;
//import gak.backend.global.util.CookieUtil;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.WebAttributes;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.rmi.server.ServerCloneException;
//import java.util.Optional;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.ws.rs.core.UriBuilder;
//import java.util.Optional;
//
//import static gak.backend.global.constants.JwtConstants.ACCESSTOKEN;
//import static gak.backend.global.constants.JwtConstants.ACCESS_TOKEN_COOKIE_EXPIRE_TIME;
//import static gak.backend.global.constants.SecurityConstants.REDIRECT_URI_PARAM_COOKIE_NAME;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationFailureHandler {
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenDao refreshTokenDao;
//    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServerCloneException{
//        String targetUrl = determineTargetUrl(request, response, authentication);
//        if(response.isCommitted()){
//            return;
//        }
//
//        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response,targetUrl);
//    }
//
////    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
////        Optional<String> redirecUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
////                .map(Cookie::getValue);
////        String targetUrl = redirecUri.orElse(getDefaultTargetUrl());
////
////        //토큰 생성
////        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
////
////        saveRefreshTokenInStorage(tokenDTO.getRefreshToken(), Long.valueOf(authentication.getName()));
////        CookieUtil.deleteCookie(request, response,ACCESSTOKEN);
////        CookieUtil.addCookie(response,ACCESSTOKEN,tokenDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME);
////        String uriString = UriComponentsBuilder.fromUriString(targetUrl)
////                .build().toUriString();
////        return uriString;
////
////    }
//protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//    Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//            .map(Cookie::getValue);
//    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
//
//    // 토큰 생성
//    TokenDTO tokenDTO = tokenProvider.createToken(authentication);
//
//    saveRefreshTokenInStorage(tokenDTO.getRefreshToken(), Long.valueOf(authentication.getName()));
//    CookieUtil.deleteCookie(request, response, ACCESSTOKEN);
//    CookieUtil.addCookie(response, ACCESSTOKEN, tokenDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME);
//
//    UriBuilder uriBuilder = UriBuilder.fromPath(targetUrl);
//    String uriString = uriBuilder.build().toString();
//    return uriString;
//}
//
//    private void saveRefreshTokenInStorage(String refreshToken, Long memberId){
//        refreshTokenDao.createRefreshToken(memberId, refreshToken);
//    }
//
//    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
////        super.clearAuthenticationAttributes(request);
////        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//        }
//    }
//
//
//}
