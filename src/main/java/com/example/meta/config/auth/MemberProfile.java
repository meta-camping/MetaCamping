package com.example.meta.config.auth;

import com.example.meta.entity.GoogleMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberProfile {
    private String name;
    private String email;
    private String provider;
    private String nickname;

    public GoogleMember toMember() {
        return GoogleMember.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .build();
    }

}