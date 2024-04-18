package com.zx.demo.services;

import com.zx.demo.controller.JobStatusController;
import com.zx.demo.data.JobStatus;
import com.zx.demo.util.JobTimeOutUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JobStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStatusService.class);

    private JobStatus jobStatus;
    private Instant jobStartTime;
    private long jobTimeoutSeconds;

    @Autowired
    private JobTimeOutUtil jobTimeOutUtil;

    public JobStatusService() {}

    public JobStatus getJobStatus() {
        checkJobStatus();
        return this.jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public long getJobTimeoutSeconds() {
        return this.jobTimeoutSeconds;
    }

    public void setJobTimeoutSeconds(long jobTimeoutSeconds) {
        this.jobTimeoutSeconds = jobTimeoutSeconds;
    }

    public Instant getJobStartTime() {
        return this.jobStartTime;
    }

    public void setJobStartTime(Instant jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public void startJob() {
//        long timeout = jobTimeOutUtil.getJobTimeout(1L, 20L);
        Instant startTime = Instant.now();
        JobStatus initialStatus = JobStatus.PENDING;

//        setJobTimeoutSeconds(timeout);
        setJobStartTime(startTime);
        setJobStatus(initialStatus);

        LOGGER.info("Job started at {}, the expected video translation time is {}s", startTime, getJobTimeoutSeconds());
    }

    private void checkJobStatus() {
        if (jobStatus == JobStatus.PENDING && Duration.between(jobStartTime, Instant.now()).getSeconds() >= jobTimeoutSeconds) {
            this.jobStatus = JobStatus.COMPLETED;
        }
    }
}
