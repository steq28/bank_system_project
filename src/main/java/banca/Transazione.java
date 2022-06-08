package banca;

import java.util.Date;
import java.util.UUID;

public class Transazione {
    private Date data;
    private String identificativo;
    private double importo;
    private String fromId;
    private String toId;

    public Transazione(Date data, double importo, String fromId, String toId) {
        this.data = data;
        this.identificativo = UUID.randomUUID().toString();
        this.importo = importo;
        this.fromId = fromId;
        this.toId = toId;
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

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

}