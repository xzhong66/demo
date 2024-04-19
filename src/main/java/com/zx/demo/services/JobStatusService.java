package com.zx.demo.services;

import com.zx.demo.controller.JobStatusController;
import com.zx.demo.data.JobStatus;
import com.zx.demo.data.entity.Job;
import com.zx.demo.data.repository.JobRepository;
import com.zx.demo.util.JobTimeOutUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Transactional
public class JobStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStatusService.class);

    private JobRepository jobRepository;

    @Autowired
    public JobStatusService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobStatus getJobStatus(long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));
        return checkAndUpdateJobStatus(job);
    }

    public long startJob(long timeout) {
        Instant startTime = Instant.now();
        JobStatus initialStatus = JobStatus.PENDING;

        Job job = new Job(initialStatus, startTime, timeout);
        jobRepository.save(job);

        LOGGER.info("Job {} started at {}, the expected video translation time is {}s", job.getId(), startTime, timeout);

        return job.getId();
    }

    private JobStatus checkAndUpdateJobStatus(Job job) {
        if(job.getJobStatus() == JobStatus.ERROR) {
            return JobStatus.ERROR;
        }

        if(job.getJobStatus() == JobStatus.COMPLETED) {
            return JobStatus.COMPLETED;
        }

        if (job.getJobStatus() == JobStatus.PENDING && Duration.between(job.getJobStartTime(), Instant.now()).getSeconds() >= job.getJobTimeoutSeconds()) {
            job.setJobStatus(JobStatus.COMPLETED);
            jobRepository.save(job);
            return JobStatus.COMPLETED;
        }

        return JobStatus.PENDING;
    }
}
