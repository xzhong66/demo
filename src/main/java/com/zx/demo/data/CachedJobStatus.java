package com.zx.demo.data;

import java.time.Instant;

public class CachedJobStatus {
    private JobStatus jobStatus;
    private Instant lastUpdated;

    public CachedJobStatus(JobStatus jobStatus, Instant lastUpdated) {
        this.jobStatus = jobStatus;
        this.lastUpdated = lastUpdated;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }
}
