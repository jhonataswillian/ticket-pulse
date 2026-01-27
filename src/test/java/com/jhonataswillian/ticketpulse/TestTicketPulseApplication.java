package com.jhonataswillian.ticketpulse;

import org.springframework.boot.SpringApplication;

public class TestTicketPulseApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketPulseApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
