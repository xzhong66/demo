package com.zx.demo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.zx.demo.client.JobStatusClient;
import com.zx.demo.data.JobStatus;
import com.zx.demo.services.JobStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private JobStatusClient jobStatusClient;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(JobStatusService jobStatusService) {
		return args -> {
			// Start the job
			jobStatusClient.startJob();

			// Check the job status every 3 seconds
			while (true) {
				String temp = jobStatusClient.getJobStatus().toString();
				String jobStatus = temp.substring(1, temp.length() - 1).split("=")[1];
				System.out.println("Job status: " + jobStatus);

				if (!jobStatus.equals(JobStatus.PENDING.toString())) {
					System.out.println("break");
					break;
				}

				Thread.sleep(3000);
			}
		};
	}

}
