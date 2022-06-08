package banca;

import java.util.Date;
import java.util.UUID;

public class Transazione {
    private Date data;
    private String identificativo;
    private double importo;
    private String beneficiarioId;

    public Transazione(Date data, double importo, String beneficiarioId) {
        this.data = data;
        this.identificativo = UUID.randomUUID().toString();
        this.importo = importo;
        this.beneficiarioId = beneficiarioId;
    }

    public Date getData() {
        return data;
    }

    public String getIdentificativo() {
        return identificativo;
    }

    public double getImporto() {
        return importo;
    }

    public String getBeneficiarioId() {
        return beneficiarioId;
    }

}