package com.zx.demo.controller;

import com.zx.demo.data.JobStatus;
import com.zx.demo.services.JobStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JobStatusController {

    @Autowired
    private JobStatusService jobStatusService;

    @GetMapping("/status")
    public ResponseEntity<Object> get() {
        try {
            JobStatus jobStatus = jobStatusService.getJobStatus();
            Map<String, JobStatus> map = new HashMap<>();
            map.put("result", jobStatus);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            jobStatusService.setJobStatus(JobStatus.ERROR);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Void> startJob(@RequestBody Map<String, Long> request) {
        try {
            jobStatusService.setJobTimeoutSeconds(request.get("timeout"));
            jobStatusService.startJob();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            jobStatusService.setJobStatus(JobStatus.ERROR);
            throw new RuntimeException(e);
        }
    }
}
