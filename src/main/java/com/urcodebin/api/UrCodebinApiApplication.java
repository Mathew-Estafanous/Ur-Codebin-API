package com.urcodebin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class UrCodebinApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrCodebinApiApplication.class, args);
	}

}
