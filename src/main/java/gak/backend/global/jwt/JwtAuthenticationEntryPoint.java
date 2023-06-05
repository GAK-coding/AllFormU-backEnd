//package gak.backend.global.jwt;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.naming.AuthenticationException;
//import java.io.IOException;
//
////유효한 자격증명을 제공하지 않고 접근할 때 401 Unauthorized error를 return
////401 Unauthorized Exception 처리를 위한 클래스
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException{
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
//    }
//}
