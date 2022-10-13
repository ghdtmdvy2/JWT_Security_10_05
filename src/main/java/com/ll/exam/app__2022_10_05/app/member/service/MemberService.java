package com.ll.exam.app__2022_10_05.app.member.service;

import com.ll.exam.app__2022_10_05.app.AppConfig;
import com.ll.exam.app__2022_10_05.app.member.entity.Member;
import com.ll.exam.app__2022_10_05.app.member.repository.MemberRepository;
import com.ll.exam.app__2022_10_05.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public String genAccessToken(Member member) {
        String accessToken = member.getAccessToken();
        // accessToken 을 가지고 있지 않다면 새로 발급.
        if (StringUtils.hasLength(accessToken) == false ) {
            accessToken = jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60L * 60 * 24 * 365 * 100);
            member.setAccessToken(accessToken);
        }

        return accessToken;
    }

    public boolean verifyWithWhiteList(Member member, String token) {
        return member.getAccessToken().equals(token);
    }
    @Cacheable("member")
    public Map<String, Object> getMemberMapByUsername__cached(String username) {
        Member member = findByUsername(username).orElse(null);

        return member.toMap();
    }

    public Member getByUsername__cached(String username) {
        // 이런식으로 해줘야 @Cacheable("member")에 있는 this 호출이 가능하다.
        MemberService thisObj = (MemberService)AppConfig.getContext().getBean("memberService");
        Map<String, Object> memberMap = thisObj.getMemberMapByUsername__cached(username);

        return Member.fromMap(memberMap);
    }
}
