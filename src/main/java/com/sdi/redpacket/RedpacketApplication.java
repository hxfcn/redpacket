package com.sdi.redpacket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sdi.common","com.sdi.redpacket.login"})
public class RedpacketApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedpacketApplication.class, args);
	}
}
