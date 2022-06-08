package banca;

import java.util.Date;

public class Transazione {
    private Date data;
    private String identificativo;
    private double importo;
    private String beneficiarioId;

    public Transazione(Date data, String indentificativo, double importo, String beneficiarioId) {
        this.data = data;
        this.identificativo = identificativo;
        this.importo = importo;
        this.beneficiarioId = beneficiarioId;
    }
}