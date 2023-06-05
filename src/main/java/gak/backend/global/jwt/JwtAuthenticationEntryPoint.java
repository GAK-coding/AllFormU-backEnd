package gak.backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//유효한 자격증명을 제공하지 않고 접근할 때 401 Unauthorized error를 return
//401 Unauthorized Exception 처리를 위한 클래스
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
//    private final ObjectMapper objectMapper = new ObjectMapper();
////    @Override
////    public void commence(HttpServletRequest request,
////                         HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException{
////        response.sendError(HttpServletResponse.SC_FORBIDDEN);
////    }
private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        sendResponse(request,response, authException);

    }

    private void sendResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException{
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}
