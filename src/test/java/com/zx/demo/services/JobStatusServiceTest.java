package com.zx.demo.services;

import com.zx.demo.data.JobStatus;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public class JobStatusServiceTest {

    private JobStatusService jobStatusService;

    @BeforeEach
    public void setup() {
        jobStatusService = new JobStatusService();
    }

    @Test
    public void testStartJob() {
        jobStatusService.startJob();
        jobStatusService.setJobTimeoutSeconds(120);

        Assertions.assertSame(jobStatusService.getJobStatus(), JobStatus.PENDING);
        Assertions.assertNotEquals(jobStatusService.getJobStartTime(), Instant.now());
    }
}
