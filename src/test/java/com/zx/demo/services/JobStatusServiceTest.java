package com.zx.demo.services;

import com.zx.demo.data.JobStatus;
import com.zx.demo.data.entity.Job;
import com.zx.demo.data.repository.JobRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class JobStatusServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobStatusService jobStatusService;

    @Test
    public void testStartJob() {
        Job testJob = new Job(JobStatus.PENDING, Instant.now(), 120);
        Mockito.when(jobRepository.findById(any())).thenReturn(Optional.of(testJob));

        long jobId = jobStatusService.startJob(120);

        Optional<Job> jobWrapper = jobRepository.findById(jobId);
        Assertions.assertTrue(jobWrapper.isPresent());
        Assertions.assertSame(jobWrapper.get().getJobStatus(), JobStatus.PENDING);
        Assertions.assertNotEquals(jobWrapper.get().getJobStartTime(), Instant.now());
    }
}
