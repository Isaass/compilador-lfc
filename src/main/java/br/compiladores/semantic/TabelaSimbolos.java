package br.compiladores.semantic;

import java.util.HashMap;
import java.util.Map;

public class TabelaSimbolos {

    private final Map<String, Simbolo> simbolos = new HashMap<>();
    private final TabelaSimbolos escopoPai;
    private int proximoDeslocamento;

    public TabelaSimbolos() {
        this(null);
    }

    public TabelaSimbolos(TabelaSimbolos escopoPai) {
        this.escopoPai = escopoPai;
        this.proximoDeslocamento = 0;
    }

    public TabelaSimbolos criarEscopoFilho() {
        return new TabelaSimbolos(this);
    }

    public TabelaSimbolos getEscopoPai() {
        return escopoPai;
    }

    public void declarar(String nome, Tipo tipo) {
        String chave = nome.toLowerCase();

        if (simbolos.containsKey(chave)) {
            throw new RuntimeException("Erro semântico: variável '" + nome + "' já foi declarada neste escopo.");
        }

        Simbolo simbolo = new Simbolo(nome, tipo, proximoDeslocamento);
        simbolos.put(chave, simbolo);

        proximoDeslocamento += tipo.getTamanhoBytes();
    }

    public Simbolo buscar(String nome) {
        String chave = nome.toLowerCase();

        Simbolo simbolo = simbolos.get(chave);

        if (simbolo != null) {
            return simbolo;
        }

        if (escopoPai != null) {
            return escopoPai.buscar(nome);
        }

        throw new RuntimeException("Erro semântico: variável '" + nome + "' não foi declarada.");
    }

    public boolean existeNoEscopoAtual(String nome) {
        return simbolos.containsKey(nome.toLowerCase());
    }

    public Map<String, Simbolo> getSimbolos() {
        return simbolos;
    }

    public void imprimir() {
        System.out.println("TABELA DE SÍMBOLOS:");
        System.out.println("--------------------------------------------------------");
        System.out.printf("%-20s %-10s %-15s%n", "VARIÁVEL", "TIPO", "DESLOCAMENTO");
        System.out.println("--------------------------------------------------------");

        for (Simbolo simbolo : simbolos.values()) {
            System.out.printf(
                    "%-20s %-10s %-15d%n",
                    simbolo.getNome(),
                    simbolo.getTipo(),
                    simbolo.getDeslocamento()
            );
        }

        System.out.println("--------------------------------------------------------");
    }
}