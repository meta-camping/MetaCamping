package com.example.firstproject.controller;

import java.util.List;

import com.example.firstproject.config.auth.PrincipalDetails;
import com.example.firstproject.dto.UpdateUserDTO;
import com.example.firstproject.entity.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
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
        return "<h1>user</h1>";
    }

    // 유저 혹은 매니저 혹은 어드민이 접근 가능- 프로필 화면 들어갈때 nickname 값을 리턴
    @GetMapping("/user/profile")
    public String userProfile(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", principal.getMember().getId());
        jsonObj.put("username", principal.getMember().getUsername());
        jsonObj.put("nickname", principal.getMember().getNickname());

        return jsonObj.toString();
    }
//@RequestParam String username,@RequestParam String password, @RequestParam String upadate_password
    @PostMapping("/user/updatePassword")
    public String updatePassword(@RequestBody UpdateUserDTO memberDTO) {
        Member member = memberRepository.findByUsername(memberDTO.getUsername());

        if (member==null) {
            return "잘못된 접근입니다";
        } else {
            if (bCryptPasswordEncoder.matches(memberDTO.getPassword(), member.getPassword())) {
                member.setPassword(bCryptPasswordEncoder.encode(memberDTO.getUpadate_password()));
                memberRepository.save(member);
                return "비밀번호 수정 완료";
            }else {
                return "기존 비밀번호를 확인하세요";
            }
        }
    }

    @PostMapping("/user/updateNickname")
    public String updateNickname(@RequestBody UpdateUserDTO memberDTO) {
        Member member = memberRepository.findByNickname(memberDTO.getNickname());
        Member member1 = memberRepository.findByNickname(memberDTO.getUpadate_nickname());
        if (member==null) {
            return "잘못된 접근입니다";
        } else {
            if (member1==null) {
                member.setNickname(memberDTO.getUpadate_nickname());
                memberRepository.save(member);
                return "닉네임 수정 완료";
            } else {
                return "이미 존재하는 닉네임 입니다";
            }
        }
    }

    // 매니저 혹은 어드민이 접근 가능
    @GetMapping("/manager")
    public String manager(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return "<h1>manager</h1>";
    }

    // 어드민이 접근 가능
    @GetMapping("/admin")
    public String admin(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return "<h1>admin</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody Member member) {
        Member isMember = memberRepository.findByUsername(member.getUsername());
        if (isMember != null) {
            return "이미 가입된 회원입니다.";
        } else {
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            member.setRoles("ROLE_USER");
            member.setNickname(member.getNickname());
            memberRepository.save(member);
            return "회원가입 완료";
        }
    }

}
