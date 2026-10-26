package com.msarvagya.ordermanagement;

import org.springframework.boot.SpringApplication;

public class TestOrdermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrderManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
