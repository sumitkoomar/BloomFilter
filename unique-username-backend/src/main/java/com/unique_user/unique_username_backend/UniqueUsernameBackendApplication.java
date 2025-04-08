package com.unique_user.unique_username_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication()
public class UniqueUsernameBackendApplication {
	public static void main(String[] args) {

		SpringApplication.run(UniqueUsernameBackendApplication.class, args);
	}

}
