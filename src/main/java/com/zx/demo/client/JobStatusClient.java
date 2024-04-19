package com.zx.demo.client;

import com.zx.demo.data.JobStatus;
import org.json.JSONObject;

public interface JobStatusClient {
    JobStatus getJobStatus();
    void startJob();
}
