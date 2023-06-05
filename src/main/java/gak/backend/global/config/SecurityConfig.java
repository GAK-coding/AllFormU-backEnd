//package gak.backend.global.config;
//
//import gak.backend.global.jwt.JwtAccessDeniedHandler;
//import gak.backend.global.jwt.JwtAuthenticationEntryPoint;
//import gak.backend.global.jwt.TokenProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig {
//    private final TokenProvider tokenProvider;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler){
//        this.tokenProvider = tokenProvider;
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return (web) -> web.ignoring()
//                .requestMatchers("/favicon.ico");
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
////    @
////    public void configure(WebSecurity web){
////        web
////                .ignoring()
////    }
//
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        return http
//                .csrf().disable()
//
//                //**401, 403 에러 핸들링
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//
//                //세션 사용 안함.
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                //HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
//                //로그인, 회원가입 등은 토큰이 없는 상태에서 요청이 들어오기 때문에 다 수용.
//                .and()
//                .authorizeRequests()
//                .requestMatchers("/register").permitAll()
//                .requestMatchers("/oauth2").permitAll()
//                .requestMatchers("/authenticate").permitAll()
//
//                //.anyRequest().permitAll()
//                .anyRequest().authenticated()
//
//                //JwtSecurityConfig
//                .and()
//                .apply(new JwtSecurityConfig(tokenProvider))
//
//
//                .and()
//                .oauth2Login()
//                .authorizationEndpoint()
//                .baseUri("/oauth2/authorization")
//                .authorizationRequestRepository()
//                .and()
//                .redirectionEndpoint()
//                .baseUri()
//                .and()
//                .userInfoEndpoint()
//                .userService()
//                .and()
//                .successHandler()
//                .failureHandler();
//
//
//        return http.build();
//
//    }
//
//
//}
