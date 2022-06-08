package lab3.esrest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsrestApplication {
	public static List<Universita> universita;
	public static List<Corso> corsi;
	
	public static void main(String[] args) {
		SpringApplication.run(EsrestApplication.class, args);
		universita = new ArrayList<Universita>();
		corsi = new ArrayList<Corso>();
		
		Esame e = new Esame();
		e.setCfu(8);
		e.nome = "SistemiDistribuiti";
		List<Esame> ee = new ArrayList<Esame>();
		ee.add(e);
		
		Corso c = new Corso("Informatica",ee);
		corsi.add(c);
		
		Universita u = new Universita("Bicocca",corsi);
		universita.add(u);
	}

}
