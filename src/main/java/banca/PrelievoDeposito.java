package banca;

import java.util.UUID;

public class PrelievoDeposito {
    private double saldo;
    private String uuidV4;

    public PrelievoDeposito(double saldo) {
        this.saldo = saldo;
        this.uuidV4 = UUID.randomUUID().toString();
    }
}
