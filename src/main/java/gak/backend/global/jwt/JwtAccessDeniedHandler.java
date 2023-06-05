package gak.backend.global.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

//필요한 권한이 존재하지 않는 경우에 403 Forbidden 에러를 리턴하기 위한 handler
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //PrintWriter writer = response.getWriter();

        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
