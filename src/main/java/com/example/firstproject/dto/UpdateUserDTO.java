package com.example.firstproject.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateUserDTO {
    @NotBlank(message = "아이디를 입력해주세요!")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요!")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
    private String password;
    private String nickname;
    private String upadate_password;
    private String upadate_nickname;

}
