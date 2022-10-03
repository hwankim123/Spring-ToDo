package HwanKim.SpringToDo.config.auth;

import HwanKim.SpringToDo.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정들을 활성화시킴
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/js/**", "/profile").permitAll()
//                .antMatchers("/tasks/**").hasRole(Role.USER.name())
//                .antMatchers("/tasks/**", "/todo/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated() //로그인된 유저만 사용 가능
                .and()
                    .logout() //로그아웃 기능에 대한 여러 설정의 진입점
                    .logoutSuccessUrl("/") //로그아웃 성공 시 '/' 주소로 이동
                .and()
                    .oauth2Login() //OAuth2 로그인 기능에 대한 여러 설정의 진입점
                    .userInfoEndpoint() //OAuth2 로그인 성공 후 사용자 정보를 가져올 때의 설정들을 담당
                    .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
    }
}
