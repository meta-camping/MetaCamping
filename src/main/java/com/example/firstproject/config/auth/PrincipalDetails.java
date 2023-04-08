package com.example.firstproject.config.auth;

//security가 /login주소 요청이 오면 낚아채서 로그인 진행시킴
//login이 완료되면 security session(security만의 session)을 만들어줌(Security ContextHolder)

import com.example.firstproject.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//1. Security Session에 들아갈 수 있는 객체는 Authentication 객체
//2. Authentication 객체 안에 User 정보를 넣어야 하는데 그 형식이 UserDetails이어야 함
//나중에 정보를 꺼낼 때는 Security Session에 있는 Authentication 객체를 꺼내고
//그 안에서 UserDetails를 꺼내면 User object(UserDetails 형식 객체)에 접근할 수 있음

//implements UserDetails를 하면 PrincipalDetails가 UserDetails 형식으로 변함
//Security Session -> Authentication -> UserDetails(PrincipalDetails)
//이렇게 되면 PrincipalDetails를 Authentication 객체 안에 넣을 수 있음
@Data
//모든 login방식을 다 받기 위해 UserDetails, OAuth2User를 둘 다 써줌, 그리고 overriding 해준다
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user; //composition
    private Map<String, Object> attributes;

    //일반 login
    public PrincipalDetails(User user) {
        this.user = user;
    }
    //OAuth login
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes=attributes;
    }

    //해당 User의 권한을 반환하는 곳
    //getAuthorities()의 형식은 Collection<? extends GrantedAuthority>인데
    //User.java의 role 형식이 String이라 바로 user.getRole()사용 불가능
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //ArrayList는 Collection의 자식
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {
        //우리 사이트 회원이 1년동안 이용 안 하면 휴면계정
        //현재시간 - login시간 = 1년 초과하면 return false 이건 다음에
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
