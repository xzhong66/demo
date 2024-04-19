package com.zx.demo.controller;

import com.zx.demo.data.JobStatus;
import com.zx.demo.data.entity.Job;
import com.zx.demo.data.repository.JobRepository;
import com.zx.demo.services.JobStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class JobStatusController {

    @Autowired
    private JobStatusService jobStatusService;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/status/{id}")
    public ResponseEntity<Object> get(@PathVariable long id) {
        try {
            JobStatus jobStatus = jobStatusService.getJobStatus(id);
            Map<String, JobStatus> map = new HashMap<>();
            map.put("result", jobStatus);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            Optional<Job> jobWrapper = jobRepository.findById(id);
            if(jobWrapper.isPresent()) {
                Job job = jobWrapper.get();
                job.setJobStatus(JobStatus.ERROR);
                jobRepository.save(job);
                throw new RuntimeException(e);
            } else {
                throw new IllegalArgumentException("Job not found with id: " + id);
            }
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Long> startJob(@RequestBody Map<String, Long> request) {
        try {
            long id = jobStatusService.startJob(request.get("timeout"));
            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
