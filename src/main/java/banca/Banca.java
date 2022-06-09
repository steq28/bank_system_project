package banca;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

@SpringBootApplication
public class Banca {
	public static List<Account> accounts;
	public static List<Transazione> transazioniTotali;
	public static Gson gson;

	public static void reset() {
		try {
			gson.toJson(accounts, new FileWriter("src/main/resources/db/accounts.json"));
			gson.toJson(transazioniTotali, new FileWriter("src/main/resources/db/transazioni.json"));
		} catch (JsonIOException e) {
			System.out.println("Error during JSON parsing");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error during IO writing");
			e.printStackTrace();
		}
		// getDbValue();
	}

	public static void getDbValue() {
		try {

			Reader accountReader = Files.newBufferedReader(Paths.get("src/main/resources/db/accounts.json"));
			Reader transazioniReader = Files.newBufferedReader(Paths.get("src/main/resources/db/transazioni.json"));
			transazioniTotali = new Gson().fromJson(transazioniReader, new TypeToken<List<Account>>() {
			}.getType());
			accounts = new Gson().fromJson(accountReader, new TypeToken<List<Account>>() {
			}.getType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error during DB loading");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Banca.class, args);

		gson = new Gson();
		getDbValue();
	}

}
