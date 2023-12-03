package com.SJY.TodayFitComplete_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //BaseTimeEntity
@SpringBootApplication
public class TodayFitCompleteBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayFitCompleteBackendApplication.class, args);
	}

}
