package lab3.esrest;

import java.util.List;

import javax.websocket.server.PathParam;

import java.util.*;
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
public class ManageCorsi {

	public Map<String, String> parseBody(String str) {
		Map<String, String> body = new HashMap<>();
		String[] values = str.split("&");
		for (int i = 0; i < values.length; ++i) {
			String[] coppia = values[i].split("=");
			if (coppia.length != 2) {
				continue;
			} else {
				body.put(coppia[0], coppia[1]);
			}
		}
		return body;
	}

	@GetMapping("/manualInput")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="api") String name, Model model) {
		model.addAttribute("name", name);
		return "manualInput";
	}
	
	@RequestMapping("/api/")
	public List<Universita> Saluta() {
		return EsrestApplication.universita;
	}

	@RequestMapping("/api/{IDuniversita}/")
	public Universita getUniversita(@PathVariable String IDuniversita) {
		Universita c = null;
		for (Universita co : EsrestApplication.universita) {
			if (co.NomeUniversita.equals(IDuniversita)) {
				c = co;
				break;
			}
		}
		if (c != null) {
			return c;
		} else {
			throw new NotFoundException();
		}
	}

	@RequestMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/")
	public Corso getCorso(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea) {
		Universita c = null;
		for (Universita co : EsrestApplication.universita) {
			if (co.NomeUniversita.equals(IDuniversita)) {
				c = co;
				break;
			}
		}
		Corso e = null;

		if (c != null) {
			for (Corso ee : c.corsi) {
				if (ee.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
					e = ee;
					break;
				}
			}
		} else {
			throw new NotFoundException();
		}
		if (e != null) {
			return e;
		} else {
			throw new NotFoundException();
		}
	}

	@RequestMapping("/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}/")
	public Esame getEsame(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea,
			@PathVariable String esame) {
		Universita u = null;
		for (Universita un : EsrestApplication.universita) {
			if (un.NomeUniversita.equals(IDuniversita)) {
				u = un;
				break;
			}
		}

		Corso c = null;
		for (Corso co : u.corsi) {
			if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
				c = co;
				break;
			}
		}
		Esame e = null;
		if (c != null) {
			for (Esame ee : c.esami) {
				if (ee.nome.equals(esame)) {
					e = ee;
					break;
				}
			}
		} else {
			throw new NotFoundException();
		}
		if (e != null) {
			return e;
		} else {
			throw new NotFoundException();
		}
	}

	@RequestMapping(value = "/api/", method = RequestMethod.POST)
	public ResponseEntity<String> addUniversita(@RequestBody String nome) {
		Map<String, String> body = parseBody(nome);
		Universita c = new Universita();
		c.NomeUniversita = body.get("nome");
		if (EsrestApplication.universita.add(c)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/api/" + c.NomeUniversita + "/");

			return new ResponseEntity<String>("OK", headers, HttpStatus.CREATED);
		} else
			return new ResponseEntity<String>("Failed", HttpStatus.OK);

	}

	@RequestMapping(value = "/api/{IDuniversita}/", method = RequestMethod.POST)
	public ResponseEntity<String> addCorso(@PathVariable String IDuniversita, @RequestBody String nome) {
		Map<String, String> body = parseBody(nome);

		Universita u = null;
		for (Universita un : EsrestApplication.universita) {
			if (un.NomeUniversita.equals(IDuniversita)) {
				u = un;
				break;
			}
		}

		if (u == null)
			return new ResponseEntity<String>("Does not exist", HttpStatus.NOT_FOUND);

		Corso c = new Corso();
		c.CorsoDiLaurea = body.get("nome");
		if (u.corsi.add(c)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/api/" + IDuniversita + "/" + c.CorsoDiLaurea + "/");

			return new ResponseEntity<String>("OK", headers, HttpStatus.CREATED);
		} else
			return new ResponseEntity<String>("Failed", HttpStatus.OK);
	}

	@RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/", method = RequestMethod.POST)
	public ResponseEntity<String> addEsame(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea,
			@RequestBody String nome) {
		Map<String, String> body = parseBody(nome);

		Universita u = null;
		for (Universita un : EsrestApplication.universita) {
			if (un.NomeUniversita.equals(IDuniversita)) {
				u = un;
				break;
			}
		}

		if (u == null)
			return new ResponseEntity<String>("Universita does not exist", HttpStatus.NOT_FOUND);

		Corso c = null;
		for (Corso co : u.corsi) {
			if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
				c = co;
				break;
			}
		}

		if (c == null)
			return new ResponseEntity<String>("Corso does not exist", HttpStatus.NOT_FOUND);

		Esame e = new Esame(body.get("nome"), Integer.parseInt(body.get("cfu")));

		if (c.esami.add(e)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/api/" + IDuniversita + "/" + IDcorsoDiLaurea + "/" + body.get("nome") + "/");

			return new ResponseEntity<String>("OK", headers, HttpStatus.CREATED);
		} else
			return new ResponseEntity<String>("Failed", HttpStatus.OK);
	}

	@RequestMapping(value = "/api/{IDuniversita}/", method = RequestMethod.DELETE)
	public String removeUniversita(@PathVariable String IDuniversita) {
		Universita c = null;
		for (Universita co : EsrestApplication.universita) {
			if (co.NomeUniversita.equals(IDuniversita)) {
				c = co;
				break;
			}
		}

		if (c != null) {
			c.corsi.clear();
			if (EsrestApplication.universita.remove(c))
				return "OK!";
			else
				return "Failed!";
		} else
			return "Failed";
	}

	@RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/", method = RequestMethod.DELETE)
	public String removeCorso(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea) {
		Universita u = null;
		for (Universita un : EsrestApplication.universita) {
			if (un.NomeUniversita.equals(IDuniversita)) {
				u = un;
				break;
			}
		}

		if (u != null) {

			Corso c = null;
			for (Corso co : u.corsi) {
				if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
					c = co;
					break;
				}
			}

			if (c != null) {
				if (u.corsi.remove(c))
					return "OK!";
				else
					return "Failed!";
			} else
				return "Failed";
		} else
			return "Failed";
	}

	@RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}/", method = RequestMethod.DELETE)
	public String removeEsame(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea,
			@PathVariable String esame) {
		Universita u = null;
		for (Universita un : EsrestApplication.universita) {
			if (un.NomeUniversita.equals(IDuniversita)) {
				u = un;
				break;
			}
		}

		if (u != null) {
			Corso c = null;
			for (Corso co : u.corsi) {
				if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
					c = co;
					break;
				}
			}

			if (c != null) {
				Esame e = null;
				for (Esame es : c.esami) {
					if (es.nome.equals(esame)) {
						e = es;
						break;
					}
				}

				if (e != null) {
					if (c.esami.remove(e))
						return "OK!";
					else
						return "Failed!";
				} else
					return "Failed";

			} else
				return "Failed";
		} else
			return "Failed";
	}

	@RequestMapping(value = "/api/{IDuniversita}/{IDcorsoDiLaurea}/{esame}/", method = RequestMethod.PUT)
	public String updateCFU(@PathVariable String IDuniversita, @PathVariable String IDcorsoDiLaurea,
			@PathVariable String esame,
			@RequestParam String cfu) {

		Universita u = null;
		for (Universita un : EsrestApplication.universita) {
			if (un.NomeUniversita.equals(IDuniversita)) {
				u = un;
				break;
			}
		}

		if (u != null) {
			Corso c = null;
			for (Corso co : u.corsi) {
				if (co.CorsoDiLaurea.equals(IDcorsoDiLaurea)) {
					c = co;
					break;
				}
			}
			Esame e = null;
			if (c != null) {
				for (Esame ee : c.esami) {
					if (ee.nome.equals(esame)) {
						e = ee;
						break;
					}
				}

				if (e != null) {
					e.cfu = Integer.parseInt(cfu);
					return "Success!";
				} else {
					return "Failed";
				}

			} else
				return "Failed";

		} else
			return "Failed";

	}
}
