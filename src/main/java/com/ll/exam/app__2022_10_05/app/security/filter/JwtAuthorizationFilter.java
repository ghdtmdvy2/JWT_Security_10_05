package com.ll.exam.app__2022_10_05.app.security.filter;

import com.ll.exam.app__2022_10_05.app.member.entity.Member;
import com.ll.exam.app__2022_10_05.app.member.service.MemberService;
import com.ll.exam.app__2022_10_05.app.security.entity.MemberContext;
import com.ll.exam.app__2022_10_05.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 가져오기.
        String bearerToken = request.getHeader("Authorization");

        // 토큰이 있다면
        if (bearerToken != null) {
            // Bearer 라는 문자열을 제거해 accessToken 만 추출
            String token = bearerToken.substring("Bearer ".length());

            // token 이 변조 되었는 지 체크(verify)
            if (jwtProvider.verify(token)) {
                // 데이터 가져오기.
                Map<String, Object> claims = jwtProvider.getClaims(token);
                // username 데이터만 가져오기.
                String username = (String) claims.get("username");
                Member member = memberService.findByUsername(username).orElseThrow(
                        () -> new UsernameNotFoundException("'%s' Username not found.".formatted(username))
                );
                // 회원이 있다면 강제 로그인.
                forceAuthentication(member);
            }
        }
        // 다음 필터로 넘기기 위한 함수. ( 커스텀 필터를 만들 때 꼭 해줘야하는 과정이다. )
        filterChain.doFilter(request, response);
    }
    // member를 가지고 강제로 로그인 시켜주는 함수.
    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        null,
                        member.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        // setContext를 해줘야한 @AuthenticationPrincipal을 이용한 memeberContext를 사용 할 수 있음.
        // 세션 방식도 이런 식으로 구현 되어 있음.
        SecurityContextHolder.setContext(context);
    }
}