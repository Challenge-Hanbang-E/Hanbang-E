package com.hanbang.e.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMember {

    private String email;

    private String address;

    public ResponseMember(String email, String address) {
        this.email = email;
        this.address = address;
    }
}