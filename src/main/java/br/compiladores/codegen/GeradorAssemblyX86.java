package br.compiladores.codegen;

import br.compiladores.semantic.Simbolo;
import br.compiladores.semantic.TabelaSimbolos;
import br.compiladores.semantic.Tipo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeradorAssemblyX86 {

    private static final Pattern ATRIBUICAO = Pattern.compile("^\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s=\\s(.+)$");
    private static final Pattern IF_ZERO = Pattern.compile("^IF\\s+(.+)\\s+==\\s+0\\s+GOTO\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");
    private static final Pattern GOTO = Pattern.compile("^GOTO\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");
    private static final Pattern LABEL = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*):$");
    private static final Pattern WRITE = Pattern.compile("^WRITE\\s+(.+)$");
    private static final Pattern READ = Pattern.compile("^READ\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");
    private static final Pattern TEMPORARIO = Pattern.compile("\\bt\\d+\\b");
    private static final Pattern STRING_LITERAL = Pattern.compile("\"[^\"]*\"");

    private final LinkedHashSet<String> temporarios = new LinkedHashSet<>();
    private final LinkedHashMap<String, String> strings = new LinkedHashMap<>();
    private int contadorStrings = 0;

    public String gerar(String codigo3AC, TabelaSimbolos tabela) {
        List<String> linhas = separarLinhas(codigo3AC);

        coletarTemporarios(linhas);
        coletarStrings(linhas);

        StringBuilder asm = new StringBuilder();

        asm.append(".386").append(System.lineSeparator());
        asm.append(".model flat, stdcall").append(System.lineSeparator());
        asm.append(".stack 4096").append(System.lineSeparator());
        asm.append(System.lineSeparator());

        asm.append("extern _print_integer:PROC").append(System.lineSeparator());
        asm.append("extern _print_string:PROC").append(System.lineSeparator());
        asm.append("extern _read_integer:PROC").append(System.lineSeparator());
        asm.append("extern _read_string:PROC").append(System.lineSeparator());
        asm.append(System.lineSeparator());

        gerarSecaoData(asm, tabela);
        gerarSecaoCode(asm, linhas, tabela);

        return asm.toString();
    }

    private List<String> separarLinhas(String codigo) {
        String[] partes = codigo.split("\\R");
        List<String> linhas = new ArrayList<>();

        for (String linha : partes) {
            String limpa = linha.trim();

            if (!limpa.isEmpty()) {
                linhas.add(limpa);
            }
        }

        return linhas;
    }

    private void coletarTemporarios(List<String> linhas) {
        for (String linha : linhas) {
            Matcher matcher = TEMPORARIO.matcher(linha);

            while (matcher.find()) {
                temporarios.add(matcher.group());
            }
        }
    }

    private void coletarStrings(List<String> linhas) {
        for (String linha : linhas) {
            Matcher matcher = STRING_LITERAL.matcher(linha);

            while (matcher.find()) {
                String literal = matcher.group();

                if (!strings.containsKey(literal)) {
                    strings.put(literal, "str" + contadorStrings++);
                }
            }
        }
    }

    private void gerarSecaoData(StringBuilder asm, TabelaSimbolos tabela) {
        asm.append(".data").append(System.lineSeparator());

        List<Simbolo> simbolos = new ArrayList<>(tabela.getSimbolos().values());
        simbolos.sort(Comparator.comparingInt(Simbolo::getDeslocamento));

        for (Simbolo simbolo : simbolos) {
            if (simbolo.getTipo() == Tipo.INTEGER) {
                asm.append("    ").append(simbolo.getNome()).append(" dw 0").append(System.lineSeparator());
            } else if (simbolo.getTipo() == Tipo.BOOLEAN) {
                asm.append("    ").append(simbolo.getNome()).append(" db 0").append(System.lineSeparator());
            } else if (simbolo.getTipo() == Tipo.STRING) {
                asm.append("    ").append(simbolo.getNome()).append(" db 256 dup(0)").append(System.lineSeparator());
            }
        }

        for (String temporario : temporarios) {
            asm.append("    ").append(temporario).append(" dw 0").append(System.lineSeparator());
        }

        for (Map.Entry<String, String> entrada : strings.entrySet()) {
            asm.append("    ")
                    .append(entrada.getValue())
                    .append(" db ")
                    .append(entrada.getKey())
                    .append(", 0")
                    .append(System.lineSeparator());
        }

        asm.append(System.lineSeparator());
    }

    private void gerarSecaoCode(StringBuilder asm, List<String> linhas, TabelaSimbolos tabela) {
        asm.append(".code").append(System.lineSeparator());
        asm.append("main PROC").append(System.lineSeparator());

        for (String linha : linhas) {
            if (linha.startsWith(";") || linha.startsWith("DECL")) {
                continue;
            }

            if (gerarLabel(asm, linha)) {
                continue;
            }

            if (gerarGoto(asm, linha)) {
                continue;
            }

            if (gerarIf(asm, linha, tabela)) {
                continue;
            }

            if (gerarRead(asm, linha, tabela)) {
                continue;
            }

            if (gerarWrite(asm, linha, tabela)) {
                continue;
            }

            if (gerarAtribuicao(asm, linha, tabela)) {
                continue;
            }

            asm.append("    ; instrução não mapeada: ").append(linha).append(System.lineSeparator());
        }

        asm.append("    ret").append(System.lineSeparator());
        asm.append("main ENDP").append(System.lineSeparator());
        asm.append("END main").append(System.lineSeparator());
    }

    private boolean gerarLabel(StringBuilder asm, String linha) {
        Matcher matcher = LABEL.matcher(linha);

        if (!matcher.matches()) {
            return false;
        }

        asm.append(matcher.group(1)).append(":").append(System.lineSeparator());
        return true;
    }

    private boolean gerarGoto(StringBuilder asm, String linha) {
        Matcher matcher = GOTO.matcher(linha);

        if (!matcher.matches()) {
            return false;
        }

        asm.append("    jmp ").append(matcher.group(1)).append(System.lineSeparator());
        return true;
    }

    private boolean gerarIf(StringBuilder asm, String linha, TabelaSimbolos tabela) {
        Matcher matcher = IF_ZERO.matcher(linha);

        if (!matcher.matches()) {
            return false;
        }

        String condicao = matcher.group(1).trim();
        String rotulo = matcher.group(2).trim();

        carregarEmAX(asm, condicao, tabela);
        asm.append("    cmp ax, 0").append(System.lineSeparator());
        asm.append("    je ").append(rotulo).append(System.lineSeparator());

        return true;
    }

    private boolean gerarRead(StringBuilder asm, String linha, TabelaSimbolos tabela) {
        Matcher matcher = READ.matcher(linha);

        if (!matcher.matches()) {
            return false;
        }

        String variavel = matcher.group(1);
        Tipo tipo = obterTipo(variavel, tabela);

        if (tipo == Tipo.STRING) {
            asm.append("    push offset ").append(variavel).append(System.lineSeparator());
            asm.append("    call _read_string").append(System.lineSeparator());
        } else {
            asm.append("    call _read_integer").append(System.lineSeparator());

            if (tipo == Tipo.BOOLEAN) {
                asm.append("    mov byte ptr [").append(variavel).append("], al").append(System.lineSeparator());
            } else {
                asm.append("    mov word ptr [").append(variavel).append("], ax").append(System.lineSeparator());
            }
        }

        return true;
    }

    private boolean gerarWrite(StringBuilder asm, String linha, TabelaSimbolos tabela) {
        Matcher matcher = WRITE.matcher(linha);

        if (!matcher.matches()) {
            return false;
        }

        String valor = matcher.group(1).trim();

        if (ehStringLiteral(valor)) {
            String label = strings.get(valor);
            asm.append("    push offset ").append(label).append(System.lineSeparator());
            asm.append("    call _print_string").append(System.lineSeparator());
            return true;
        }

        carregarEmAX(asm, valor, tabela);
        asm.append("    push ax").append(System.lineSeparator());
        asm.append("    call _print_integer").append(System.lineSeparator());

        return true;
    }

    private boolean gerarAtribuicao(StringBuilder asm, String linha, TabelaSimbolos tabela) {
        Matcher matcher = ATRIBUICAO.matcher(linha);

        if (!matcher.matches()) {
            return false;
        }

        String destino = matcher.group(1).trim();
        String expressao = matcher.group(2).trim();

        if (ehStringLiteral(expressao)) {
            asm.append("    ; atribuição de string não expandida: ").append(linha).append(System.lineSeparator());
            return true;
        }

        if (gerarOperacaoBinaria(asm, destino, expressao, tabela)) {
            return true;
        }

        if (expressao.startsWith("~")) {
            String valor = expressao.substring(1).trim();
            carregarEmAX(asm, valor, tabela);
            asm.append("    cmp ax, 0").append(System.lineSeparator());
            asm.append("    sete al").append(System.lineSeparator());
            asm.append("    xor ah, ah").append(System.lineSeparator());
            armazenarDestino(asm, destino, tabela);
            return true;
        }

        if (expressao.startsWith("-") && !ehNumero(expressao)) {
            String valor = expressao.substring(1).trim();
            carregarEmAX(asm, valor, tabela);
            asm.append("    neg ax").append(System.lineSeparator());
            armazenarDestino(asm, destino, tabela);
            return true;
        }

        carregarEmAX(asm, expressao, tabela);
        armazenarDestino(asm, destino, tabela);

        return true;
    }

    private boolean gerarOperacaoBinaria(StringBuilder asm, String destino, String expressao, TabelaSimbolos tabela) {
        String[] operadores = {">=", "<=", "==", "<>", "AND", "OR", "+", "-", "*", "/", ">", "<"};

        for (String operador : operadores) {
            if (!expressao.contains(" " + operador + " ")) {
                continue;
            }

            String[] partes = expressao.split("\\s+" + Pattern.quote(operador) + "\\s+");

            if (partes.length != 2) {
                continue;
            }

            String esquerda = partes[0].trim();
            String direita = partes[1].trim();

            carregarEmAX(asm, esquerda, tabela);
            carregarEmBX(asm, direita, tabela);

            switch (operador) {
                case "+":
                    asm.append("    add ax, bx").append(System.lineSeparator());
                    break;
                case "-":
                    asm.append("    sub ax, bx").append(System.lineSeparator());
                    break;
                case "*":
                    asm.append("    imul bx").append(System.lineSeparator());
                    break;
                case "/":
                    asm.append("    cwd").append(System.lineSeparator());
                    asm.append("    idiv bx").append(System.lineSeparator());
                    break;
                case ">":
                    gerarComparacao(asm, "setg");
                    break;
                case "<":
                    gerarComparacao(asm, "setl");
                    break;
                case ">=":
                    gerarComparacao(asm, "setge");
                    break;
                case "<=":
                    gerarComparacao(asm, "setle");
                    break;
                case "==":
                    gerarComparacao(asm, "sete");
                    break;
                case "<>":
                    gerarComparacao(asm, "setne");
                    break;
                case "AND":
                    asm.append("    and ax, bx").append(System.lineSeparator());
                    asm.append("    cmp ax, 0").append(System.lineSeparator());
                    asm.append("    setne al").append(System.lineSeparator());
                    asm.append("    xor ah, ah").append(System.lineSeparator());
                    break;
                case "OR":
                    asm.append("    or ax, bx").append(System.lineSeparator());
                    asm.append("    cmp ax, 0").append(System.lineSeparator());
                    asm.append("    setne al").append(System.lineSeparator());
                    asm.append("    xor ah, ah").append(System.lineSeparator());
                    break;
                default:
                    return false;
            }

            armazenarDestino(asm, destino, tabela);
            return true;
        }

        return false;
    }

    private void gerarComparacao(StringBuilder asm, String instrucaoSet) {
        asm.append("    cmp ax, bx").append(System.lineSeparator());
        asm.append("    ").append(instrucaoSet).append(" al").append(System.lineSeparator());
        asm.append("    xor ah, ah").append(System.lineSeparator());
    }

    private void carregarEmAX(StringBuilder asm, String operando, TabelaSimbolos tabela) {
        if (ehNumero(operando)) {
            asm.append("    mov ax, ").append(operando).append(System.lineSeparator());
            return;
        }

        Tipo tipo = obterTipo(operando, tabela);

        if (tipo == Tipo.BOOLEAN) {
            asm.append("    mov al, byte ptr [").append(operando).append("]").append(System.lineSeparator());
            asm.append("    xor ah, ah").append(System.lineSeparator());
        } else {
            asm.append("    mov ax, word ptr [").append(operando).append("]").append(System.lineSeparator());
        }
    }

    private void carregarEmBX(StringBuilder asm, String operando, TabelaSimbolos tabela) {
        if (ehNumero(operando)) {
            asm.append("    mov bx, ").append(operando).append(System.lineSeparator());
            return;
        }

        Tipo tipo = obterTipo(operando, tabela);

        if (tipo == Tipo.BOOLEAN) {
            asm.append("    mov bl, byte ptr [").append(operando).append("]").append(System.lineSeparator());
            asm.append("    xor bh, bh").append(System.lineSeparator());
        } else {
            asm.append("    mov bx, word ptr [").append(operando).append("]").append(System.lineSeparator());
        }
    }

    private void armazenarDestino(StringBuilder asm, String destino, TabelaSimbolos tabela) {
        Tipo tipo = obterTipo(destino, tabela);

        if (tipo == Tipo.BOOLEAN) {
            asm.append("    mov byte ptr [").append(destino).append("], al").append(System.lineSeparator());
        } else {
            asm.append("    mov word ptr [").append(destino).append("], ax").append(System.lineSeparator());
        }
    }

    private Tipo obterTipo(String nome, TabelaSimbolos tabela) {
        if (nome.matches("t\\d+")) {
            return Tipo.INTEGER;
        }

        try {
            return tabela.buscar(nome).getTipo();
        } catch (RuntimeException e) {
            return Tipo.INTEGER;
        }
    }

    private boolean ehNumero(String valor) {
        return valor.matches("-?\\d+");
    }

    private boolean ehStringLiteral(String valor) {
        return valor.startsWith("\"") && valor.endsWith("\"");
    }
}