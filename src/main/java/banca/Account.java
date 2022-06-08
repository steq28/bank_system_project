package banca;

public class Account {
	private String name;
	private String surname;
	private String accountId; //hex random generator
	
	public Account(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
		this.accountId = "5"; //hex
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
	
}
