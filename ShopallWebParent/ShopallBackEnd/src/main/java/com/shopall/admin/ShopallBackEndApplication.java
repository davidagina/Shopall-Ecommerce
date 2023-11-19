package com.shopall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopall.common.entity", "com.shopall.admin.user"})
public class ShopallBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopallBackEndApplication.class, args);
	}

}
