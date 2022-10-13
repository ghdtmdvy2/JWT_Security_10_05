package com.ll.exam.app__2022_10_05.app.cacheTest.service;

import com.ll.exam.app__2022_10_05.app.cacheTest.dto.Person;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheTestService {

    @Cacheable("key1")
    public int getCachedInt() {
        System.out.println("getCachedInt 호출됨");
        return 5;
    }

    @CacheEvict("key1")
    public void deleteCacheKey1() {
    }

    @CachePut("key1")
    public int putCacheKey1() {
        return 10;
    }

    @Cacheable("plus")
    public int plus(int a, int b) {
        System.out.println("== plus 실행 ==");
        return a + b;
    }

    @Cacheable(value = "getName", key = "#p.id")
    public String getName(Person p, int random) {
        System.out.println("== getName 실행됨 ==");
        return p.getName();
    }
}