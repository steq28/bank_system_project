package banca;

import java.util.UUID;

public class PrelievoDeposito {
    private double saldo;
    private String uuidV4;

    public PrelievoDeposito(double saldo) {
        this.saldo = saldo;
        this.uuidV4 = UUID.randomUUID().toString();
    }

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public String getUuidV4() {
		return uuidV4;
	}
    
}
