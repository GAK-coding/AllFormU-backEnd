package gak.backend.global.config;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dao.RefreshTokenDao;
//import gak.backend.domain.member.application.OAuth2UserService;
//import gak.backend.domain.oauth.dao.OAuth2AuthorizationRequestBasedOnCookieRepository;
//import gak.backend.domain.oauth.handler.OAuth2AuthenticationFailureHandler;
//import gak.backend.domain.oauth.handler.OAuthAuthenticationSuccessHandler;
import gak.backend.global.jwt.JwtAccessDeniedHandler;
import gak.backend.global.jwt.JwtAuthenticationEntryPoint;
import gak.backend.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final RefreshTokenDao refreshTokenDao;
    private final MemberRepository memberRepository;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    //private final OAuth2UserService oAuth2UserService;


//    public SecurityConfig(TokenProvider tokenProvider, RefreshTokenDao refreshTokenDao, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
//        this.tokenProvider = tokenProvider;
//        this.refreshTokenDao = refreshTokenDao;
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.ico");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @
//    public void configure(WebSecurity web){
//        web
//                .ignoring()
//    }

//    @Bean
//    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler(){
//        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
//    }
//
//    @Bean
//    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository(){
//        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
//    }

//    @Bean
//    public OAuthAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(){
//        return new OAuthAuthenticationSuccessHandler(
//                tokenProvider,
//                refreshTokenDao,
//                oAuth2AuthorizationRequestBasedOnCookieRepository()
//        );
//    }



    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()

                //**401, 403 에러 핸들링
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //세션 사용 안함.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
                //로그인, 회원가입 등은 토큰이 없는 상태에서 요청이 들어오기 때문에 다 수용.
                .and()
                //TODO 조건 더 촘촘하게 할 것.
                .authorizeRequests()
                .requestMatchers("/member/**").permitAll()
                .requestMatchers("/form/**").authenticated()
                .requestMatchers("/response/**").authenticated()
                .requestMatchers("/member/oauth2").permitAll()
                .requestMatchers("/member/login").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/member/register").permitAll()
                .requestMatchers("/authenticate").permitAll()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))

//                .and()
//                .formLogin()
//                .loginPage("/member/login")
//                .usernameParameter("email")
//                .passwordParameter("PW")
//                .loginPage("member/login")
//                .defaultSuccessUrl("/")
//                .failureUrl("/member/login")

                .and()
                .logout()
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .and().build();


//                .and()
//                .oauth2Login()
//                .loginPage("/member/oauth")
//                .defaultSuccessUrl("/")
//                .failureUrl("/member/oauth")
//                .userInfoEndpoint()
//                .userService(oAuth2UserService)
//                .and()
//                .successHandler(oAuth2AuthenticationSuccessHandler())
//                .failureHandler(oAuth2AuthenticationFailureHandler());

//                .anyRequest().permitAll()

                //JwtSecurityConfig



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

    }


}
