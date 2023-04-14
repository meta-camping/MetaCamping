package com.example.firstproject.controller;

import java.util.List;

import com.example.firstproject.config.auth.PrincipalDetails;
import com.example.firstproject.entity.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 모든 사람이 접근 가능
    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
    // 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.

    // 유저 혹은 매니저 혹은 어드민이 접근 가능
    @GetMapping("/user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal : "+principal.getMember().getId());
        System.out.println("principal : "+principal.getMember().getUsername());
        System.out.println("principal : "+principal.getMember().getPassword());

        return "<h1>user</h1>";
    }

    // 매니저 혹은 어드민이 접근 가능
    @GetMapping("/manager/reports")
    public String reports() {
        return "<h1>reports</h1>";
    }

    // 어드민이 접근 가능
    @GetMapping("/admin/users")
    public List<Member> users(){
        return memberRepository.findAll();
    }

    @PostMapping("/join")
    public String join(@RequestBody Member member) {
        Member isMember = memberRepository.findByUsername(member.getUsername());
        if (isMember != null) {
            return "이미 가입된 회원입니다.";
        } else {
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            member.setRoles("ROLE_USER");
            memberRepository.save(member);
            return "회원가입 완료";
        }
    }

}
