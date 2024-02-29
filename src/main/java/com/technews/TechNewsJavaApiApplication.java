package com.technews;

import com.technews.seedData.SeedDataLoader;
import com.technews.seedData.SeedDataRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TechNewsJavaApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(TechNewsJavaApiApplication.class, args);
//		SeedDataLoader seedDataLoader = new SeedDataLoader();
//		seedDataLoader.loadSeedData();
	}

}
