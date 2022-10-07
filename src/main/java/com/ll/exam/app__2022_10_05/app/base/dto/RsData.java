package com.ll.exam.app__2022_10_05.app.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RsData<T> {
    // 상태 코드 : F-2 <- 실패의 두번째 것
    // 상태 코드 : S-3 <- 성공의 세번째 것
    private String resultCode;
    // 상태 메세지
    private String msg;
    private T data;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }
    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    // 성공 했는 지 알려주는 함수.
    public boolean isSuccess() {
        return resultCode.startsWith("S-1");
    }

    public boolean isFail() {
        return isSuccess() == false;
    }
}