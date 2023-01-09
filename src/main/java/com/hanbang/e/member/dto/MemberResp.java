package com.hanbang.e.member.dto;

import com.hanbang.e.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResp {

    private String email;
    private String address;

    public MemberResp(String email, String address) {
        this.email = email;
        this.address = address;
    }

    public static MemberResp from(Member member) {
        return new MemberResp(member.getEmail(), member.getAddress());
    }
}