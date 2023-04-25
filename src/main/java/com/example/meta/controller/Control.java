package com.example.meta.controller;

import com.example.meta.dto.MemberDto;
import com.example.meta.entity.Member;
import com.example.meta.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
@RestController
@Log4j2
@RequiredArgsConstructor
public class Control {
    private final MemberService memberService;
    @PostMapping("/onRegister")
    public String regist(MemberDto dto) {
        //log.info(dto);
        //dto를 entity 로 변환 (변환 자체는 toEntity()메서드 이다.)
        MemberDto created = memberService.create(dto);

//        System.out.println(created);
        return "회원가입 완료";
    }

    @PostMapping("/onLogin")
    public String login(@RequestParam String user_id, @RequestParam String user_pw) {
        log.info(user_id + "/" + user_pw);
        String result_id = memberService.login_id(user_id);
        String result_pw = memberService.login_pw(user_pw);

        return result_id + " / " + result_pw;
    }

    @DeleteMapping("/ondelete/{member_id}")
    public void deleteMemeberId(@PathVariable Long member_id, MemberDto dto) {
        memberService.delete(dto);
    }
//

@PutMapping("/onupdate/{member_id}")
public void update(@PathVariable Long member_id, @RequestBody MemberDto dto) {
    memberService.update(dto);
}


}

