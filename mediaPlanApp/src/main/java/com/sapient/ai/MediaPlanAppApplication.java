package com.sapient.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaPlanAppApplication {

	public static void main(String[] args) {
		System.setProperty("cyc.session.server", "cyc.sapient-ai.com:3600");
		SpringApplication.run(MediaPlanAppApplication.class, args);
	}
}
