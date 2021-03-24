package es.ucm.fdi.iw.g01.bayshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BayshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BayshopApplication.class, args);
	}

}
