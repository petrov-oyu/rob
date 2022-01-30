package ru.petrovoyu.bestpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class BestPracticeApplication {

	public static void main(String[] args) {
		int[][] array = new int[3][3];
		System.err.println(1/0);

		Collections.sort(List.of("2", "10" ,"abc"));
		SpringApplication.run(BestPracticeApplication.class, args);
	}

}
