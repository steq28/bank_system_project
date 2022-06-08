package banca;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Banca {
	public static List<Account> accounts;

	public static void main(String[] args) {
		SpringApplication.run(Banca.class, args);
		accounts = new ArrayList<Account>();
	}

}
