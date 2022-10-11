package com.ll.exam.app__2022_10_05.app.member.controller;

import com.ll.exam.app__2022_10_05.app.base.dto.RsData;
import com.ll.exam.app__2022_10_05.app.member.dto.request.LoginDto;
import com.ll.exam.app__2022_10_05.app.member.entity.Member;
import com.ll.exam.app__2022_10_05.app.member.service.MemberService;
import com.ll.exam.app__2022_10_05.app.security.entity.MemberContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.ll.exam.app__2022_10_05.util.Util;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/me")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext) {
        if (memberContext == null) { // 임시코드, 나중에는 시프링 시큐리티를 이용해서 로그인을 안했다면, 아예 여기로 못 들어오도록
            return Util.spring.responseEntityOf(RsData.failOf(null));
        }

        return Util.spring.responseEntityOf(RsData.successOf(memberContext));
    }
    @PostMapping("/login")
    public ResponseEntity<RsData> login(@RequestBody LoginDto loginDto) {
        if (loginDto.isNotValid()) {
            // 첫번째 파라미터 : ResponseEntity 의 body 는 화면상 보여주는 내용
            // 두번째 파라미터 : headers 는 브라우저 내에서 보여주는 내용
            // 세번째 파라미터 : 브라우저의 상태코드를 보여주는 내용

            // 첫번째 파라미터를 객체로 보내주어, 알아서 자동으로 Json 형태로 뿌려주는 방법을 할 수 있다.
            return Util.spring.responseEntityOf(RsData.of("F-1", "로그인 정보가 올바르지 않습니다."));
        }

        Member member = memberService.findByUsername(loginDto.getUsername()).orElse(null);

        if (member == null) {
            return Util.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if (passwordEncoder.matches(loginDto.getPassword(), member.getPassword()) == false) {
            return Util.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        String accessToken = memberService.genAccessToken(member);

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Util.mapOf(
                                "accessToken", accessToken
                        )
                ),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }


}