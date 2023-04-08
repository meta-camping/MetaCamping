package com.example.firstproject.config.auth;


import com.example.firstproject.entity.User;
import com.example.firstproject.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//SecurityConfig에서 loginProcessingUrl("/login");
//login 요청이 오면 자동으로 UserDetailsService 형식으로 IoC 되어있는 loadUserByUsername method가 실행(규칙임)
//loginForm.html에서 name="username"으로 적었으므로 스프링이 PrincipalDetailsService를 찾아서 loadUserByUsername을 호출,
// 그때 넘어온 username 매개변수를 가지고 옴, username을 찾음
@Log4j2
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    //Security Session내부에 Authentication이, Authentication 내부에 UserDetails(PrincipalDetails)가 들어감
    //함수 종료시 @AuthenticationPrincipal이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username25L = " + username);
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
