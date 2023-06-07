//package gak.backend.domain.oauth.handler;
//
//import gak.backend.domain.member.application.OAuth2UserService;
//import gak.backend.domain.oauth.dao.OAuth2AuthorizationRequestBasedOnCookieRepository;
//import gak.backend.global.util.CookieUtil;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import javax.naming.AuthenticationException;
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
//
//    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
//        String targetUrl = CookieUtil.getCookie(request, "redirect_uri")
//                .map(Cookie::getValue)
//                .orElse(("/"));
//
//        exception.printStackTrace();
//
//        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("error", exception.getLocalizedMessage())
//                .build().toUriString();
//
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//    }
//}
