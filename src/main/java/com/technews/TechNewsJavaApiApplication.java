package com.technews;

import com.technews.seedData.SeedDataLoader;
import com.technews.seedData.SeedDataRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
@SpringBootApplication
public class TechNewsJavaApiApplication {
	static boolean isSchemaDropped = false;

	public static void main(String[] args) {
		if (!isSchemaDropped) {
			dropSchema();
		}

		SpringApplication.run(TechNewsJavaApiApplication.class, args);

		if (isSchemaDropped) {
			SeedDataRunner.SeedRunner(args);
		}
	}
	public static void dropSchema() {
		// Use JDBC or JPA to execute SQL commands to drop the schema
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "apothocare")) {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP DATABASE IF EXISTS just_tech_news_java_db");
			System.out.println("Database dropped successfully");
			isSchemaDropped = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
