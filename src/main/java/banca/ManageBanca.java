package banca;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpHeaders;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import org.springframework.ui.Model;

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

	// Endpoint GET "/api/account"
	@RequestMapping(value = "/api/account", method = RequestMethod.GET)
	public List<Account> getAccount() {
		return Banca.accounts;
	}

	// Endpoint POST "/api/account"
	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public void createAccount(@RequestBody String bodyRaw) {

		String uniqueID = UUID.randomUUID().toString(); // se escono uguali faccio la rinuncia agli studi

		Map<String, String> body = parseBody(bodyRaw);

		Account ac = new Account(body.get("name"), body.get("surname"), uniqueID);

		Banca.accounts.add(ac);
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
			if (Banca.accounts.remove(removeThis))
				return new ResponseEntity<String>("OK", HttpStatus.OK);
			else
				return new ResponseEntity<String>("Failed to remove", HttpStatus.BAD_REQUEST);
		} else
			return new ResponseEntity<String>("Account not found!", HttpStatus.NOT_FOUND);
	}

	// Endpoint GET "/api/account/{accountId}"
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<Proprietario> getAccountDetails(@PathVariable String accountId) {
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

			// return proprietario;
			return new ResponseEntity<Proprietario>(proprietario, HttpStatus.OK);

		} else {
			// TODO: ritorna un errore di "account non trovato"
			return new ResponseEntity<Proprietario>(new Proprietario("", "", -1.0, null), HttpStatus.BAD_REQUEST);
		}
	}

	// Endpoint POST "/api/account/{accountId}" for Prelevare/Depositare
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.POST)
	public ResponseEntity prelevaDeposita(@PathVariable String accountId, @RequestBody String bodyRaw) {

		Map<String, String> body = parseBody(bodyRaw);

		double amount = Double.parseDouble((body.get("amount")));

		Account accountTrovato = null;
		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}
		if (accountTrovato != null) {
			double saldo = accountTrovato.getSaldo();
			if (amount < 0 && saldo < amount) {
				// if the value is -1 its an error
				return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(-1), HttpStatus.NOT_ACCEPTABLE);
			} else {
				saldo += amount;
				accountTrovato.setSaldo(saldo);
				return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(saldo), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(-1), HttpStatus.NOT_ACCEPTABLE);
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
					return new ResponseEntity<String>("OK!", HttpStatus.OK);
				} else {
					if (body.containsKey("surname")) {
						accountTrovato.setSurname(body.get("surname"));
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
		Map<String, String> body = parseBody(bodyRaw);
		Account accountTrovato = null;

		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				accountTrovato = ac;
				break;
			}
		}

		if (accountTrovato != null) {
			return new ResponseEntity<String>(accountTrovato.getName() + " " + accountTrovato.getSurname(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
		}
	}

	// Endpoint POST "/api/transfer" for transfer money
	@RequestMapping(value = "/api/transfer", method = RequestMethod.POST)
	public ResponseEntity tranfer(@RequestBody String bodyRaw) {
		// from
		// to
		// amount
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
		if (accountTrovato != null) {
			double saldo = accountTrovato.getSaldo();
			double saldo2 = account2.getSaldo();
			if (amount < 0 && saldo < amount) {
				// if the value is -1 its an error
				return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
			} else if (amount > 0) {

				Transazione t = new Transazione(new Date(System.currentTimeMillis()), amount,
						accountTrovato.getAccountId(), account2.getAccountId());

				accountTrovato.addTransazione(t);
				account2.addTransazione(t);

				saldo += amount;
				accountTrovato.setSaldo(saldo);
				account2.setSaldo(saldo2);
				return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(saldo), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<PrelievoDeposito>(new PrelievoDeposito(-1), HttpStatus.NOT_ACCEPTABLE);
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
					mittente.setSaldo(-transazioneCanc.getImporto());
					beneficiario.setSaldo(transazioneCanc.getImporto());
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