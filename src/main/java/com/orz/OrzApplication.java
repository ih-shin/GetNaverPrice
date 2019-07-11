package com.orz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan("com.orz")
@SpringBootApplication
public class OrzApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrzApplication.class, args);
	}

}
