package com.zx.demo.util;

import org.springframework.stereotype.Component;

@Component
public class JobTimeOutUtil {
    public long getJobTimeout(long leftLimit, long rightLimit) {
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
