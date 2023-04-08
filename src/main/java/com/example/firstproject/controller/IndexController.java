package com.example.firstproject.controller;


import com.example.firstproject.config.auth.PrincipalDetails;
import com.example.firstproject.entity.User;
import com.example.firstproject.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller //View를 리턴
public class IndexController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    @ResponseBody
    //@AuthenticationPrincipal을 통해 session 정보에 접근 가능
    //PrincipalDetails는 UserDetails를 implements했기 때문에 UserDetails 대신에 PrincipalDetails 사용 가능
    //일반 login은 @AuthenticationPrincipal UserDetails로 받는다
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {//DI(의존성 주입)
        log.info("/test/login =========== ");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        //principalDetails는 user를 변수로 받아서 getUser()사용
        log.info("principalDetails.getUser()36L = " + principalDetails.getUser());
        //@AuthenticationPrincipal을 통해 getUser()사용 가능
        log.info("userDetails38L = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    //social login은 @AuthenticationPrincipal OAuth2User 로 받는다
    public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {//DI(의존성 주입)
        log.info("/test/oauth/login =========== ");
        //getPrincipal()을 통해 OAuth2User로 down casting하면 사용자 정보를 받을 수 있고,
        //그 정보는 loadUser의 super.loadUser(userRequest).getAttributes()에서 받아온 정보임
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authenticatio48L = " + oAuth2User.getAttributes());
        log.info("OAuth2User51 = " + oauth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    //OAuth login을 해도 PrincipalDetails로 받을 수 있고
    //일반 login을 해도 PrincipalDetails로 받을 수 있음
    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails = "+ principalDetails.getUser());
        return "user";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @ResponseBody
    @PostMapping("/join")
    public String join(User user) {
        log.info("user55L = " + user);
        user.setRole("ROLE_USER");
        //회원가입 되지만 비밀번호 노출돼서 password암호화가 되지않음 security로 login 불가능
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "Register Successed";
    }

    @Secured("ROLE_ADMIN")//특정 method에 간단하게 접근제한 부여
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    //data라는 method가 실행되기 직전에 실행됨
    @PreAuthorize("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "data 정보";
    }
}
