package com.zx.demo.client;

import com.zx.demo.util.JobTimeOutUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobStatusClientImpl implements JobStatusClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStatusClientImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${job.api.url}")
    private String jobApiUrl;

    @Autowired
    private JobTimeOutUtil jobTimeOutUtil;

    @Override
    public Object getJobStatus() {
        try {
            return restTemplate.getForObject(jobApiUrl + "/status", Object.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startJob() {
        try {
            Map<String, Long> request = new HashMap<>();
            long timeToFinishJob = jobTimeOutUtil.getJobTimeout(1L, 20L);
            request.put("timeout", timeToFinishJob);

            LOGGER.info("The expected time to finish the job is {}", timeToFinishJob);

            restTemplate.postForObject(jobApiUrl + "/start", request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
