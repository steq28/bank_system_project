package banca;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@RequestMapping(value = "/api/account", method = RequestMethod.GET)
	public List<Account> getAccount() {
		return Banca.accounts;
	}

	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public void createAccount(@RequestBody String nome) {
		Map<String, String> body = parseBody(nome);

		Account ac = new Account(body.get("name"), body.get("surname"));

		Banca.accounts.add(ac);
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.DELETE)
	public String removeEsame(@PathVariable String accountId) {
		Account removeThis = null;

		for (Account ac : Banca.accounts) {
			if (ac.getAccountId().equals(accountId)) {
				removeThis = ac;
				break;
			}
		}

		if (removeThis != null) {
			if (Banca.accounts.remove(removeThis))
				return "OK!";
			else
				return "Failed!";
		} else
			return "Failed";
	}

	// @GetMapping("/manualInput")
	// public String greeting(@RequestParam(name = "name", required = false,
	// defaultValue = "api") String name,
	// Model model) {
	// model.addAttribute("name", name);
	// return "manualInput";
	// }

	// @RequestMapping("/api/")
	// public List<Universita> Saluta() {
	// return EsrestApplication.universita;
	// }

	// @RequestMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}/")
	// public Esame getEsame(@PathVariable String IDuniversita, @PathVariable String
	// IDcorsoDiLaurea,

	// @PathVariable String esame) {
	// Universita u = null;
	// for (Universita un : EsrestApplication.universita) {
	// if (un.NomeUniversita.equals(IDuniversita)) {
	// u = un;
	// break;
	// }
	// }

	// Corso c = null;
	// for (Corso co : u.corsi) {
	// if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
	// c = co;
	// break;
	// }
	// }
	// Esame e = null;
	// if (c != null) {
	// for (Esame ee : c.esami) {
	// if (ee.nome.equals(esame)) {
	// e = ee;
	// break;
	// }
	// }
	// } else {
	// throw new NotFoundException();
	// }
	// if (e != null) {
	// return e;
	// } else {
	// throw new NotFoundException();
	// }
	// }

	// @RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/", method =
	// RequestMethod.POST)
	// public ResponseEntity<String> addEsame(@PathVariable String IDuniversita,
	// @PathVariable String IDcorsoDiLaurea,

	// @RequestBody String nome) {
	// Map<String, String> body = parseBody(nome);

	// Universita u = null;
	// for (Universita un : EsrestApplication.universita) {
	// if (un.NomeUniversita.equals(IDuniversita)) {
	// u = un;
	// break;
	// }
	// }

	// if (u == null)
	// return new ResponseEntity<String>("Universita does not exist",
	// HttpStatus.NOT_FOUND);

	// Corso c = null;
	// for (Corso co : u.corsi) {
	// if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
	// c = co;
	// break;
	// }
	// }

	// if (c == null)
	// return new ResponseEntity<String>("Corso does not exist",
	// HttpStatus.NOT_FOUND);

	// Esame e = new Esame(body.get("nome"), Integer.parseInt(body.get("cfu")));

	// if (c.esami.add(e)) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Location", "/api/" + IDuniversita + "/" + IDcorsoDiLaurea + "/"
	// + body.get("nome") + "/");

	// return new ResponseEntity<String>("OK", headers, HttpStatus.CREATED);
	// } else
	// return new ResponseEntity<String>("Failed", HttpStatus.OK);
	// }

	// @RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}/",
	// method = RequestMethod.DELETE)
	// public String removeEsame(@PathVariable String IDuniversita, @PathVariable
	// String IDcorsoDiLaurea,

	// @PathVariable String esame) {
	// Universita u = null;
	// for (Universita un : EsrestApplication.universita) {
	// if (un.NomeUniversita.equals(IDuniversita)) {
	// u = un;
	// break;
	// }
	// }

	// if (u != null) {
	// Corso c = null;
	// for (Corso co : u.corsi) {
	// if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
	// c = co;
	// break;
	// }
	// }

	// if (c != null) {
	// Esame e = null;
	// for (Esame es : c.esami) {
	// if (es.nome.equals(esame)) {
	// e = es;
	// break;
	// }
	// }

	// if (e != null) {
	// if (c.esami.remove(e))
	// return "OK!";
	// else
	// return "Failed!";
	// } else
	// return "Failed";

	// } else
	// return "Failed";
	// } else
	// return "Failed";
	// }

	// @RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}/",
	// method = RequestMethod.PUT)
	// public String updateCFU(@PathVariable String IDuniversita, @PathVariable
	// String IDcorsoDiLaurea,

	// @PathVariable String esame,

	// @RequestParam String cfu) {

	// Universita u = null;
	// for (Universita un : EsrestApplication.universita) {
	// if (un.NomeUniversita.equals(IDuniversita)) {
	// u = un;
	// break;
	// }
	// }

	// if (u != null) {
	// Corso c = null;
	// for (Corso co : u.corsi) {
	// if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
	// c = co;
	// break;
	// }
	// }
	// Esame e = null;
	// if (c != null) {
	// for (Esame ee : c.esami) {
	// if (ee.nome.equals(esame)) {
	// e = ee;
	// break;
	// }
	// }

	// if (e != null) {
	// e.cfu = Integer.parseInt(cfu);
	// return "Success!";
	// } else {
	// return "Failed";
	// }

	// } else
	// return "Failed";

	// } else
	// return "Failed";

	// }

}
