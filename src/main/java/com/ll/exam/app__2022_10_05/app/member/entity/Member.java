package com.ll.exam.app__2022_10_05.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.app__2022_10_05.app.base.entity.BaseEntity;
import com.ll.exam.app__2022_10_05.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;

    public Member(long id) {
        super(id);
    }

    // 현재 회원이 가지고 있는 권한들을 List<GrantedAuthority> 형태로 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return authorities;
    }
    public static Member fromJwtClaims(Map<String, Object> jwtClaims) {
        long id = (long)(int)jwtClaims.get("id");
        LocalDateTime createDate = Util.date.bitsToLocalDateTime((List<Integer>)jwtClaims.get("createDate"));
        LocalDateTime modifyDate = Util.date.bitsToLocalDateTime((List<Integer>)jwtClaims.get("modifyDate"));
        String username = (String)jwtClaims.get("username");
        String email = (String)jwtClaims.get("email");

        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .modifyDate(modifyDate)
                .username(username)
                .email(email)
                .build();
    }
    // jwt 내용 안에 넣어주는 함수.
    public Map<String, Object> getAccessTokenClaims() {
        return Util.mapOf(
                "id", getId(),
                "createDate", getCreateDate(),
                "modifyDate", getModifyDate(),
                "username", getUsername(),
                "email", getEmail(),
                "authorities", getAuthorities()
        );
    }
}
