package banca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
	private String name;
	private String surname;
	private String accountId;
	private Double saldo;
	private List<Transazione> transazioni;

	public Account(String name, String surname, String accountId) {
		super();
		this.name = name;
		this.surname = surname;
		this.accountId = accountId;
		this.saldo = 0.0;
		this.transazioni = new ArrayList<Transazione>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAccountId() {
		return accountId;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public void addTransazione(Transazione t) {
		transazioni.add(t);
		transazioni.sort((t1, t2) -> t2.getData().compareTo(t1.getData()));
	}

	public List<Transazione> getTransazioni() {
		return transazioni;
	}
}
