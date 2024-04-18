package com.zx.demo.client;

import com.zx.demo.data.JobStatus;

public interface JobStatusClient {
    Object getJobStatus();
    void startJob();
}
