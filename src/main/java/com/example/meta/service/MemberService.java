package com.example.meta.service;

import com.example.meta.repository.MemberRepository;
import com.example.meta.dto.MemberDto;
import com.example.meta.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class MemberService {
@Autowired
MemberRepository memberRepository;

    public MemberDto create(MemberDto dto) {
        Member member = dto.toEntity();
        //System.out.println("member : "+ member);
        Member result = memberRepository.save(member);
        log.info(result);
       MemberDto r = new MemberDto(result);
        return r;
    }
//public MemberDto create(MemberDto dto) throws DataIntegrityViolationException {
//    try {
//        Member member = dto.toEntity();
//        Member result = memberRepository.save(member);
//        log.info(result);
//        return new MemberDto(result);
//    } catch (DataIntegrityViolationException ex) {
//        log.error("message : {}", ex.getMessage());
//        // 중복된 값 저장시 DataIntegrityViolationException 발생
//        throw ex;
//    } catch (Exception ex) {
//        // 그 외의 예외 처리
//        throw new RuntimeException(ex);
//    }
//}

    public String login_id(String id) {
        String result_id = memberRepository.findById(id);
        if (result_id == null) {
            return "일치하는 아이디가 없습니다";
        } else {
            return "아이디가 일치합니다";
        }
    }

    public String login_pw(String pw) {
        String result_pw = memberRepository.findByPw(pw);
        if (result_pw == null) {
            return "일치하는 비밀번호가 없습니다";
        } else {
            return "비밀번호가 일치합니다";
        }
    }
//    public String delete_id(String memberId) {
//        String delete_id = memberRepository.deleteById(memberId);
//     return null;
//    }

    public void delete(MemberDto dto) {
        memberRepository.delete(dto.toEntity());
    }

    public MemberDto update(MemberDto dto) throws DataIntegrityViolationException {
        try {
            //Optional<Member>는 밑에 isPresent()가 포함된 if문(존재하는지 안 하는지)을 쓰기
            //위해 사용, 그래서 findById를 사용하여 있는지 없는지 먼저 찾는 거임
            Optional<Member> result = memberRepository.findById(dto.getMember_id());
            if (result.isPresent()) {
                Member m = result.get();
                //getCreate_time을 setCreate_time에 대입
                // Create_time은 입력하지 않아도 된다 .(클라이언트에서) (Create_time 은 DB에서 Not Null로 입력했기 때문)
                dto.setCreate_time(m.getCreate_time());
                Member member = dto.toEntity();
                Member r = memberRepository.save(member);
                log.info(r);
                return new MemberDto(r);
            } else {
                return null;
            }
        } catch (DataIntegrityViolationException ex) {
            log.error("message : {}", ex.getMessage());
            // 중복된 값 저장시 DataIntegrityViolationException 발생
            throw ex;
        } catch (Exception ex) {
            // 그 외의 예외 처리
            throw new RuntimeException(ex);
        }
    }
//    public String delete(Long memberId) {
//        memberRepository.deleteById(memberId);
//        return "회원탈퇴 완료";
//    }



}
