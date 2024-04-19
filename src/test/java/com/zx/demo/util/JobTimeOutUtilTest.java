package com.zx.demo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JobTimeOutUtilTest {

    private JobTimeOutUtil jobTimeOutUtil;

    @Test
    public void testGetJobTimeout() {
        jobTimeOutUtil = new JobTimeOutUtil();
        long timeout = jobTimeOutUtil.getJobTimeout(1L, 10L);
        Assertions.assertTrue(timeout >= 1L);
        Assertions.assertTrue(timeout <= 10L);
    }
}
