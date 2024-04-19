package com.zx.demo;

import com.zx.demo.client.JobStatusClient;
import com.zx.demo.data.JobStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {

    @Value("${job.api.url}")
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobStatusClient jobStatusClient;

    @Test
    void testJobStatusFlow() throws JSONException {
        // Start a new job
        long jobId = jobStatusClient.startJob();

        // Check initial job status
        JobStatus jobStatus = jobStatusClient.getJobStatus(jobId);
        Assertions.assertEquals(JobStatus.PENDING, jobStatus);

        // Wait for the job to complete or fail
        while (jobStatus == JobStatus.PENDING) {
            System.out.println("Job id and status: " + jobId + " " + jobStatus);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jobStatus = jobStatusClient.getJobStatus(jobId);
        }
        System.out.println("Job id and status: " + jobId + " " + jobStatus);

        // Verify final job status
        ResponseEntity<Object> response = restTemplate.getForEntity(baseUrl + "/status/" + jobId, Object.class);
        String src = Objects.requireNonNull(response.getBody()).toString().replace("=", ":");
        JSONObject jsonObject = new JSONObject(src);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(jobStatus, JobStatus.fromString(jsonObject.getString("result")));
    }
}
