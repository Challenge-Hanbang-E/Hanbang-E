package com.hanbang.e.member.dto;

import com.hanbang.e.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class MemberLoginReq {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!#%*?&])[A-Za-z\\d@$!#%*?&]{8,15}$",
            message = "비밀번호는 8~15자리의 대소문자,숫자,특수문자로 이루어져야 합니다.")
    private String password;

    public MemberLoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
