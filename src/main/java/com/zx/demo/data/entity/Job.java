package com.zx.demo.data.entity;

import com.zx.demo.data.JobStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

/***
 * JPA entity storing job specific information
 */
@Entity
public class Job {

    public Job(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private JobStatus jobStatus;

    // the start time of a job
    private Instant jobStartTime;

    // the expected time taken to finish the job
    private long jobTimeoutSeconds;

    public Job(JobStatus jobStatus, Instant jobStartTime, long jobTimeoutSeconds) {
        this.jobStatus = jobStatus;
        this.jobStartTime = jobStartTime;
        this.jobTimeoutSeconds = jobTimeoutSeconds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Instant getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(Instant jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public long getJobTimeoutSeconds() {
        return jobTimeoutSeconds;
    }

    public void setJobTimeoutSeconds(long jobTimeoutSeconds) {
        this.jobTimeoutSeconds = jobTimeoutSeconds;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobStatus=" + jobStatus +
                ", jobStartTime=" + jobStartTime +
                ", jobTimeoutSeconds=" + jobTimeoutSeconds +
                '}';
    }
}
