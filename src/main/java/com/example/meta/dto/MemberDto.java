package com.example.meta.dto;

import com.example.meta.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude
public class MemberDto {
    private Long member_id;

    private String id;
    private String email;
    private String password;
    private String sns_id;
    private String nickname;
    private int role;
    private LocalDateTime create_time;
    private LocalDateTime update_time;

    //Dto를 Entity로 변환해주는 메서드
    public Member toEntity() {
        return new Member(member_id,id,email,password,sns_id,nickname,role,create_time,update_time);
    }

    //Entity에서 Dto 로 변환해주는 메서드
    public MemberDto(Member member){
        this.member_id = member.getMember_id();
        this.id=member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.sns_id = member.getSns_id();
        this.nickname = member.getNickname();
        this.role = member.getRole();
        this.create_time = member.getCreate_time();
        this.update_time = member.getUpdate_time();
    }
}