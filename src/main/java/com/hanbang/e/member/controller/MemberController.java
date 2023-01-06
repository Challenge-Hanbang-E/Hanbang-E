package com.hanbang.e.member.controller;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.member.dto.RequestCreateMember;
import com.hanbang.e.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid RequestCreateMember requestCreateMember) {
        memberService.signup(requestCreateMember);
        return new ResponseEntity<>(new ResponseDto<>("success", "회원가입 완료", null), HttpStatus.OK);
    }

}