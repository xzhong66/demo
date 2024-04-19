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

    /***
     * Get job status from DB
     * @param jobId
     * @return job status
     */
    public JobStatus getJobStatus(long jobId) {
        // query DB to get the requested job. If present, return. Else, throw IllegalArgumentException
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));
        // check and update status of the job
        return checkAndUpdateJobStatus(job);
    }

    /***
     * Create a job and save it to DB
     * @param timeout the expected time taken to finish the job
     * @return the ID of newly created job
     */
    public long startJob(long timeout) {
        Instant startTime = Instant.now();
        JobStatus initialStatus = JobStatus.PENDING;

        // set up new job
        Job job = new Job(initialStatus, startTime, timeout);

        // save to DB
        jobRepository.save(job);

        LOGGER.info("Job {} started at {}, the expected video translation time is {}s", job.getId(), startTime, timeout);

        return job.getId();
    }

    /***
     * check and update job status based on start time and timeout
     * @param job the requested job
     * @return the updated job status
     */
    private JobStatus checkAndUpdateJobStatus(Job job) {
        // no action needed if job status is ERROR and COMPLETED
        if(job.getJobStatus() == JobStatus.ERROR) {
            return JobStatus.ERROR;
        }

        if(job.getJobStatus() == JobStatus.COMPLETED) {
            return JobStatus.COMPLETED;
        }

        // If job status is pending and the job is actually finished, we need to update it to COMPLETED and save to DB
        if (job.getJobStatus() == JobStatus.PENDING && Duration.between(job.getJobStartTime(), Instant.now()).getSeconds() >= job.getJobTimeoutSeconds()) {
            // update status
            job.setJobStatus(JobStatus.COMPLETED);
            // save to DB
            jobRepository.save(job);
            return JobStatus.COMPLETED;
        }

        return JobStatus.PENDING;
    }
}
