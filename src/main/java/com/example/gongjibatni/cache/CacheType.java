package com.example.gongjibatni.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    ACADEMINC_NOTICE("academicNotice", 2, 1);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}

