package banca;

import java.util.List;

class Proprietario {
    public String nome;
    public String cognome;
    public Double saldo;
    public List<Transazione> transazioni;

    public Proprietario(String nome, String cognome, Double saldo, List<Transazione> transazioni) {
        this.nome = nome;
        this.cognome = cognome;
        this.saldo = saldo;
        this.transazioni = transazioni;
    }
}
