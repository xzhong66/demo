package com.zx.demo.util;

import org.springframework.stereotype.Component;

/***
 * This is to simulate the video translation time. Return a random number in a specified range.
 */
@Component
public class JobTimeOutUtil {
    public long getJobTimeout(long leftLimit, long rightLimit) {
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
