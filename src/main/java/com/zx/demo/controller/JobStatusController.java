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

    /***
     * Handling GET request
     * @param id jobID
     * @return a responseEntity indicating the job status
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<Object> get(@PathVariable long id) {
        try {
            // get status
            JobStatus jobStatus = jobStatusService.getJobStatus(id);

            // reconstruct result
            Map<String, JobStatus> map = new HashMap<>();
            map.put("result", jobStatus);

            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            // Error handling:
            // 1. If the requested job is in DB, set the status to ERROR
            // 2. If not, throw IllegalArgumentException
            Optional<Job> jobWrapper = jobRepository.findById(id);
            if(jobWrapper.isPresent()) {
                // if present
                Job job = jobWrapper.get();
                // set to ERROR status and save to DB
                job.setJobStatus(JobStatus.ERROR);
                jobRepository.save(job);
                throw new RuntimeException(e);
            } else {
                //if not present
                throw new IllegalArgumentException("Job not found with id: " + id);
            }
        }
    }

    /***
     * Handling POST request
     * @param request requestBody sent with POST request
     * @return a responseEntity indicating the jobID
     */
    @PostMapping("/start")
    public ResponseEntity<Long> startJob(@RequestBody Map<String, Long> request) {
        try {
            // start new job and get id
            long id = jobStatusService.startJob(request.get("timeout"));

            // return response
            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
