package banca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.http.HttpHeaders;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Exchanger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManageBanca {

	public Map<String, String> parseBody(String str) {
		Map<String, String> body = new HashMap<>();
		String[] values = str.split("&");
		for (int i = 0; i < values.length; ++i) {
			String[] coppia = values[i].split("=");
			if (coppia.length == 2) {
				body.put(coppia[0], coppia[1]);
			}
		}
		return body;
	}

	// Endpoint GET "/" main
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String index() throws IOException, URISyntaxException {
		URL res = getClass().getClassLoader().getResource("index.html");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	// Endpoint GET "/transfer" transfer
	@RequestMapping(method = RequestMethod.GET, value = "/transfer")
	public String transfer() throws IOException, URISyntaxException {
		URL res = getClass().getClassLoader().getResource("transfer.html");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	// Endpoint GET "/list" list
	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public String list() throws IOException, URISyntaxException {
		URL res = getClass().getClassLoader().getResource("list.html");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	// Endpoint GET "/register" register
	@RequestMapping(method = RequestMethod.GET, value = "/register")
	public String register() throws IOException, URISyntaxException {
		URL res = getClass().getClassLoader().getResource("register.html");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	// Endpoint GET "/api/account"
	@RequestMapping(value = "/api/account", method = RequestMethod.GET)
	public List<Account> getAccount() {
		return Banca.accounts;
	}

	// Endpoint POST "/api/account"
	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public ResponseEntity createAccount(@RequestBody String bodyRaw) {
		boolean found = true;
		String uniqueID;
		do {
			uniqueID = UUID.randomUUID().toString();

			uniqueID = uniqueID.replace("-", "");
			uniqueID = uniqueID.substring(0, 20);

			String app = uniqueID;

			found = Banca.accounts.stream()
					.anyMatch(p -> p.getAccountId().equals(app));

		} while (found);

		Map<String, String> body = parseBody(bodyRaw);
		Account ac;
		try {
			ac = new Account(body.get("name"), body.get("surname"), uniqueID);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed parsing data", HttpStatus.BAD_REQUEST);
		}

		Banca.accounts.add(ac);
		Banca.reset();

		return new ResponseEntity<String>("{accountId: \"" + uniqueID + "\"}", HttpStatus.OK);
	}

	@RequestMapping(value = "/api/account", method = RequestMethod.DELETE)
	public ResponseEntity removeAccount(@RequestParam("id") String accountId) {
		Account removeThis = null;

		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				removeThis = ac;
				break;
			}
		}

		if (removeThis != null) {
			if (Banca.accounts.remove(removeThis)) {
				Banca.reset();
				return new ResponseEntity<String>("OK", HttpStatus.OK);
			} else
				return new ResponseEntity<String>("Failed to remove", HttpStatus.BAD_REQUEST);
		} else
			return new ResponseEntity<String>("Account non trovato!", HttpStatus.NOT_FOUND);
	}

	// Endpoint GET "/api/account/{accountId}"
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.GET)
	public ResponseEntity getAccountDetails(@PathVariable String accountId) {
		Account accountTrovato = null;
		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}

		if (accountTrovato != null) {
			Proprietario proprietario = new Proprietario(accountTrovato.getName(), accountTrovato.getSurname(),
					accountTrovato.getSaldo(), accountTrovato.getTransazioni());

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("X-Sistema-Bancario", accountTrovato.getName() + ";" + accountTrovato.getSurname());
			return new ResponseEntity<Proprietario>(proprietario, responseHeaders, HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("Account non trovato!", HttpStatus.NOT_FOUND);
		}
	}

	// Endpoint POST "/api/account/{accountId}" for Prelevare/Depositare
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.POST)
	public ResponseEntity prelevaDeposita(@PathVariable String accountId,
			@RequestBody String bodyRaw) {

		double amount;
		try {
			Map<String, String> body = parseBody(bodyRaw);

			amount = Double.parseDouble((body.get("amount")));
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed parsing data", HttpStatus.BAD_REQUEST);
		}

		Account accountTrovato = null;
		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}

		if (accountTrovato != null) {
			double saldo = accountTrovato.getSaldo();
			if (amount < 0 && saldo < (-1 * amount)) {
				return new ResponseEntity<String>("Saldo non sufficiente!", HttpStatus.NOT_ACCEPTABLE);
			} else {
				saldo += amount;
				accountTrovato.setSaldo(saldo);
				Banca.reset();
				return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(saldo), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<String>("Account non trovato!", HttpStatus.NOT_FOUND);
		}
	}

	// Endpoint PUT "/api/account/{accountId}"
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.PUT)
	public ResponseEntity<String> editAccount(@PathVariable String accountId, @RequestBody String bodyRaw) {

		Map<String, String> body = parseBody(bodyRaw);
		Account accountTrovato = null;

		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}

		if (accountTrovato != null && body.containsKey("name") && body.containsKey("surname")) {
			accountTrovato.setName(body.get("name"));
			accountTrovato.setSurname(body.get("surname"));
			Banca.reset();
			return new ResponseEntity<String>("OK!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail!", HttpStatus.BAD_REQUEST);
		}
	}

	// Endpoint PATCH "/api/account/{accountId}"
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.PATCH)
	public ResponseEntity<String> editAccountField(@PathVariable String accountId, @RequestBody String bodyRaw) {
		Map<String, String> body = parseBody(bodyRaw);
		Account accountTrovato = null;

		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}

		if (accountTrovato != null) {
			if (body.size() == 1) {
				if (body.containsKey("name")) {
					accountTrovato.setName(body.get("name"));
					Banca.reset();
					return new ResponseEntity<String>("OK!", HttpStatus.OK);
				} else {
					if (body.containsKey("surname")) {
						accountTrovato.setSurname(body.get("surname"));
						Banca.reset();
						return new ResponseEntity<String>("OK!", HttpStatus.OK);
					} else
						return new ResponseEntity<String>("Chiave errata!", HttpStatus.NOT_ACCEPTABLE);
				}
			} else {
				return new ResponseEntity<String>("Errore numero chiavi!", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Account non trovato!", HttpStatus.NOT_FOUND);
		}
	}

	// Endpoint HEAD "/api/account/{accountId}"
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.HEAD)
	public ResponseEntity getNameAndSurname(@PathVariable String accountId, @RequestBody String bodyRaw) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, String> body = parseBody(bodyRaw);
		Account accountTrovato = null;

		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}

		if (accountTrovato != null) {

			responseHeaders.set("X-Sistema-Bancario",
					accountTrovato.getName() + ";" + accountTrovato.getSurname());
			return new ResponseEntity<Void>(responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	// Endpoint POST "/api/transfer" for transfer money
	@RequestMapping(value = "/api/transfer", method = RequestMethod.POST)
	public ResponseEntity tranfer(@RequestBody String bodyRaw) {
		Map<String, String> body = parseBody(bodyRaw);

		double amount = Double.parseDouble((body.get("amount")));
		String from = body.get("from");
		String to = body.get("to");

		Account accountTrovato = null;
		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(from)) {
				accountTrovato = ac;
				break;
			}
		}

		Account account2 = null;
		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(to)) {
				account2 = ac;
				break;
			}
		}

		if (accountTrovato != null && account2 != null) {
			double saldo = accountTrovato.getSaldo();
			double saldo2 = account2.getSaldo();
			if (amount < 0 || saldo < amount) {
				// if the value is -1 its an error
				return new ResponseEntity<String>("Saldo non sufficiente per effettuare la transazione!",
						HttpStatus.NOT_ACCEPTABLE);
			} else if (amount > 0) {

				Transazione t = new Transazione(new Date(System.currentTimeMillis()), amount,
						accountTrovato.getAccountId(), account2.getAccountId());

				accountTrovato.addTransazione(t);
				account2.addTransazione(t);
				Banca.transazioniTotali.add(t);

				saldo -= amount;
				saldo2 += amount;
				accountTrovato.setSaldo(saldo);
				account2.setSaldo(saldo2);
				Banca.reset();
				// TODO sistemare in base al file
				return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(saldo), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Importo deve essere > 0", HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<String>("Account non trovato!", HttpStatus.NOT_ACCEPTABLE);
		}
	}

	// Endpoint POST "/api/divert"
	@RequestMapping(value = "/api/divert", method = RequestMethod.POST)
	public ResponseEntity<String> annullaTransazione(@RequestBody String bodyRaw) {

		Map<String, String> body = parseBody(bodyRaw);

		String id = body.get("id");
		Transazione transazioneCanc = null;

		for (Transazione t : Banca.transazioniTotali) {
			if (t.getIdentificativo().equals(id)) {
				transazioneCanc = t;
				break;
			}
		}

		Account beneficiario = null, mittente = null;

		if (transazioneCanc != null) {
			for (Account ac : Banca.accounts) {
				if (ac.getAccountId().equals(transazioneCanc.getToId())) {
					mittente = ac;
				} else {
					if (ac.getAccountId().equals(transazioneCanc.getFromId())) {
						beneficiario = ac;
					}
				}
				if (mittente != null && beneficiario != null)
					break;
			}

			if (mittente.getSaldo() >= transazioneCanc.getImporto()) {
				Transazione nuovaTransazione = new Transazione(new Date(System.currentTimeMillis()),
						transazioneCanc.getImporto(), transazioneCanc.getToId(), transazioneCanc.getFromId());
				if (Banca.transazioniTotali.add(nuovaTransazione)) {
					mittente.setSaldo(mittente.getSaldo() - transazioneCanc.getImporto());
					beneficiario.setSaldo(beneficiario.getSaldo() + transazioneCanc.getImporto());

					mittente.addTransazione(nuovaTransazione);
					beneficiario.addTransazione(nuovaTransazione);
					Banca.transazioniTotali.add(nuovaTransazione);
					Banca.reset();
					return new ResponseEntity<String>("Transazione annullata correttamente!", HttpStatus.OK);
				} else
					return new ResponseEntity<String>("Inserimento non riuscito!",
							HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<String>("Importo del beneficiario non sufficiente!",
						HttpStatus.NOT_ACCEPTABLE);
			}

		} else {
			return new ResponseEntity<String>("Transazione non trovata!", HttpStatus.NOT_FOUND);
		}
	}
}