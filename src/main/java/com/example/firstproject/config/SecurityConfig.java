package com.example.firstproject.config;


import com.example.firstproject.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

// 로그인이 완료되면 순서가
//1. code 받기(인증)
//2. code를 통해서 access token 받고, access token을 받으면 우리의 security server가 google에 login한 사용자의 정보에 접근할 수 있는 권한이 생겼다는 뜻(권한)
//3. 권한을 통해 사용자 profile 정보를 가져와서
//4-1. 그 정보를 토대로 회원가입을 자동으로 시키기도 하고
//4-2. 추가 정보를 받기도 함. 예를 들어 google에서 email, 전화번호, 이름, id를 줬는데 쇼핑몰에서 집 주소도 필요한 경우 정보를 받아야함

@EnableWebSecurity//spring security filter가 spring filterchain에 등록됨
@Configuration
//securedEnabled = true -> secured annotation 활성화
//prePostEnabled = true -> preAuthorize annotation 활성화, postAuthorize annotation도 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                //login 한 사람만 들어올 수 있음, authenticated()인증만 되면 들어갈 수 있는 주소
                .antMatchers("/user/**").authenticated()
                //admin 또는 manager권한이 있어야 들어올 수 있음
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                //admin만 가능
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                //위 3가지 주소가 아니면 누구나 가능
                .anyRequest().permitAll()
                //위 3가지 주소가 아니면 loginForm으로 이동?
                .and()
                .formLogin()
                .defaultSuccessUrl("http://localhost:3000/")
                .failureUrl("/login?error")
                .loginPage("/login")
                .loginProcessingUrl("/dologin")
                .and()
                .logout()
                .logoutSuccessUrl("/");


//                .loginPage("/login")
//                //1. /login으로 이동할 때 loginForm.html의 login, password를 가져옴
//                //2. login 주소가 호출되면 security가 낚아채서 대신 로그인 진행해줌,
//                //controller에 /login 안 만들어도 됨
//                .loginProcessingUrl("/login")
//                //loginForm.html의 name="username"을 username2로 바꾸려면 usernameParameter()로 바꿔줘야함
//                //.usernameParameter("username2")
//                .defaultSuccessUrl("/");
//                //여기서부터 google login 가능 코드
//                .and()
//                .oauth2Login()
//                .loginPage("/login")
//                .userInfoEndpoint()
//                .userService(principalOauth2UserService);//google login이 완료된 후 처리가 필요, google login이 완료되면 (access token+사용자profile) 정보를 바로 받아줌
        return http.build();
    }
}
