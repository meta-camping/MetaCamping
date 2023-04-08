package com.example.firstproject.config.oauth;


import com.example.firstproject.config.auth.PrincipalDetails;
import com.example.firstproject.entity.User;
import com.example.firstproject.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    //google로부터 받은 userRequest 정보에 대한 후처리를 하는 loadUser
    //함수 종료시 @AuthenticationPrincipal이 만들어짐
    @Override
    //(access token+사용자profile) 정보가 userRequest에 반환
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //registrationId로 어떤 OAuth로 login 했는지 확인 가능
        log.info("getClientRegistration17L = " + userRequest.getClientRegistration());
        log.info("getTokenValue18L = " + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //google login을 누르면 -> google login화면 -> login 완료 -> code를 반환(OAuth-Client library) -> AccessToken 요청
        //userRequest정보 -> loadUser를 호출 -> google로부터 회원정보를 받아줌
        log.info("getAttributes24 = " + oAuth2User.getAttributes());
        //강제로 회원가입 할 예정
        String provider = userRequest.getClientRegistration().getClientId();//google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;//google_sub
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            log.info("google login이 최초");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }else{
            log.info("구글 로그인 한 적이 있습니다.");
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
