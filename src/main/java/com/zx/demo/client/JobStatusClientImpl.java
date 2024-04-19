package com.zx.demo.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zx.demo.data.CachedJobStatus;
import com.zx.demo.data.JobStatus;
import com.zx.demo.util.JobTimeOutUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JobStatusClientImpl implements JobStatusClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStatusClientImpl.class);

    private RestTemplate restTemplate;

    private String jobApiUrl;

    private JobTimeOutUtil jobTimeOutUtil;

    private Cache<Long, CachedJobStatus> jobStatusCache;

    @Autowired
    public JobStatusClientImpl(RestTemplate restTemplate,
                               @Value("${job.api.url}") String jobApiUrl,
                               @Value("${job.status.cache.expiration.seconds}") int cacheExpirationSeconds,
                               JobTimeOutUtil jobTimeOutUtil) {
        this.restTemplate = restTemplate;
        this.jobApiUrl = jobApiUrl;

        this.jobStatusCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(cacheExpirationSeconds))
                .build();

        this.jobTimeOutUtil = jobTimeOutUtil;
    }

    @Override
    public JobStatus getJobStatus(long id) {
        try {
            return jobStatusCache.get(id, () -> {

                LOGGER.info("Cache miss, calling API service to retrieve job status.");

                String src = Objects.requireNonNull(restTemplate.getForObject(jobApiUrl + "/status/" + id, Object.class)).toString().replace("=", ":");
                JSONObject jsonObject = new JSONObject(src);
                return new CachedJobStatus(JobStatus.fromString(jsonObject.getString("result")), Instant.now());
            }).getJobStatus();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long startJob() {
        try {
            Map<String, Long> request = new HashMap<>();
            long timeToFinishJob = jobTimeOutUtil.getJobTimeout(10L, 60L);
            request.put("timeout", timeToFinishJob);

            long id = restTemplate.postForObject(jobApiUrl + "/start", request, Long.class);
            LOGGER.info("The expected time to finish the job {} is {}s", id, timeToFinishJob);

            jobStatusCache.put(id, new CachedJobStatus(JobStatus.PENDING, Instant.now()));
            LOGGER.info("Put job status to cache when job started.");

            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
