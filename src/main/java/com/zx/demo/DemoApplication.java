package com.zx.demo;

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

	/***
	 * This is to simulate the integration of a third party app calling Job Status API
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {

			// 3 is an arbitrary number for demo purpose
			int jobs = 3;

			while(jobs-- > 0 ) {
				// Start the job
				long id = jobStatusClient.startJob();

				// Check the job status every 3 seconds
				while (true) {
					JobStatus jobStatus = jobStatusClient.getJobStatus(id);
					System.out.println("Job id and status: " + id + " " + jobStatus);

					if (jobStatus != JobStatus.PENDING) {
						System.out.println("break");
						break;
					}

					Thread.sleep(3000);
				}
			}
		};
	}

}
