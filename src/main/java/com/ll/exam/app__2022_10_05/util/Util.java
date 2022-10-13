package com.ll.exam.app__2022_10_05.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.exam.app__2022_10_05.app.AppConfig;
import com.ll.exam.app__2022_10_05.app.base.dto.RsData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Util {
    private static ObjectMapper getObjectMapper() {
        return (ObjectMapper) AppConfig.getContext().getBean("objectMapper");
    }

    public static class date {

        public static LocalDateTime bitsToLocalDateTime(List<Integer> bits) {
            return LocalDateTime.of(bits.get(0), bits.get(1), bits.get(2), bits.get(3), bits.get(4), bits.get(5), bits.get(6));
        }
    }
    public static class json {

        public static Object toStr(Map<String, Object> map) {
            try {
                return getObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Map<String, Object> toMap(String jsonStr) {
            try {
                return getObjectMapper().readValue(jsonStr, LinkedHashMap.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    // Object... args : 모든 파라미터의 값을 싹다 받아올 수 있는 파라미터이다.
    public static <K, V> Map<K, V> mapOf(Object... args) {
        Map<K, V> map = new LinkedHashMap<>();

        int size = args.length / 2;

        for (int i = 0; i < size; i++) {
            int keyIndex = i * 2;
            int valueIndex = keyIndex + 1;

            K key = (K) args[keyIndex];
            V value = (V) args[valueIndex];

            map.put(key, value);
        }

        return map;
    }
    public static class spring {

        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData) {
            return responseEntityOf(rsData, null);
        }

        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData, HttpHeaders headers) {
            // 첫번째 파라미터 : ResponseEntity 의 body 는 화면상 보여주는 내용
            // 두번째 파라미터 : headers 는 브라우저 내에서 보여주는 내용
            // 세번째 파라미터 : 브라우저의 상태코드를 보여주는 내용

            // 첫번째 파라미터를 객체로 보내주어, 알아서 자동으로 Json 형태로 뿌려주는 방법을 할 수 있다.
            return new ResponseEntity<>(rsData, headers, rsData.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        }

        public static HttpHeaders httpHeadersOf(String... args) {
            HttpHeaders headers = new HttpHeaders();

            Map<String, String> map = Util.mapOf(args);

            for (String key : map.keySet()) {
                String value = map.get(key);
                headers.set(key, value);
            }

            return headers;
        }
    }
}