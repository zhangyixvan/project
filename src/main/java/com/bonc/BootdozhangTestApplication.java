package com.bonc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bonc.mapper")
public class BootdozhangTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootdozhangTestApplication.class, args);
	}
}
