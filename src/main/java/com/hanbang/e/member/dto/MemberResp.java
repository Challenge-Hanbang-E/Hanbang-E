package com.hanbang.e.member.dto;

import lombok.Getter;

@Getter
public class MemberResp {

    private String email;
    private String address;

    public MemberResp(String email, String address) {
        this.email = email;
        this.address = address;
    }
    public static MemberResp of(String email, String address) {
        return new MemberResp(email, address);
    }
}