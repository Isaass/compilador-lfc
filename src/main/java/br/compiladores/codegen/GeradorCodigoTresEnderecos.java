package br.compiladores.codegen;

import br.compiladores.parser.SimpleLangBaseVisitor;
import br.compiladores.parser.SimpleLangParser;

public class GeradorCodigoTresEnderecos extends SimpleLangBaseVisitor<String> {

    private final StringBuilder codigo = new StringBuilder();
    private int contadorTemporarios = 0;
    private int contadorRotulos = 0;

    public String getCodigo() {
        return codigo.toString();
    }

    private void emitir(String linha) {
        codigo.append(linha).append(System.lineSeparator());
    }

    private String novoTemporario() {
        return "t" + contadorTemporarios++;
    }

    private String novoRotulo() {
        return "L" + contadorRotulos++;
    }

    @Override
    public String visitPrograma(SimpleLangParser.ProgramaContext ctx) {
        emitir("; Código intermediário em três endereços");
        emitir("; Início do programa");

        if (ctx.declaracoes() != null) {
            visit(ctx.declaracoes());
        }

        visit(ctx.comandoComposto());

        emitir("; Fim do programa");

        return null;
    }

    @Override
    public String visitDeclaracao(SimpleLangParser.DeclaracaoContext ctx) {
        String tipo = ctx.tipo().getText().toUpperCase();

        for (var tokenId : ctx.listaIds().ID()) {
            String nome = tokenId.getText();
            emitir("DECL " + nome + " : " + tipo);
        }

        return null;
    }

    @Override
    public String visitComandoComposto(SimpleLangParser.ComandoCompostoContext ctx) {
        if (ctx.listaComandos() != null) {
            visit(ctx.listaComandos());
        }

        return null;
    }

    @Override
    public String visitComandoAtribuicao(SimpleLangParser.ComandoAtribuicaoContext ctx) {
        String destino = ctx.ID().getText();
        String valor = visit(ctx.expressao());

        emitir(destino + " = " + valor);

        return null;
    }

    @Override
    public String visitComandoRead(SimpleLangParser.ComandoReadContext ctx) {
        for (var tokenId : ctx.listaIds().ID()) {
            emitir("READ " + tokenId.getText());
        }

        return null;
    }

    @Override
    public String visitComandoWrite(SimpleLangParser.ComandoWriteContext ctx) {
        for (var expressao : ctx.listaWrite().expressao()) {
            String valor = visit(expressao);
            emitir("WRITE " + valor);
        }

        return null;
    }

    @Override
    public String visitComandoIf(SimpleLangParser.ComandoIfContext ctx) {
        String rotuloElse = novoRotulo();
        String rotuloFim = novoRotulo();

        String condicao = visit(ctx.expressao());

        emitir("IF " + condicao + " == 0 GOTO " + rotuloElse);

        visit(ctx.comando(0));

        if (ctx.comando().size() > 1) {
            emitir("GOTO " + rotuloFim);
            emitir(rotuloElse + ":");
            visit(ctx.comando(1));
            emitir(rotuloFim + ":");
        } else {
            emitir(rotuloElse + ":");
        }

        return null;
    }

    @Override
    public String visitComandoWhile(SimpleLangParser.ComandoWhileContext ctx) {
        String rotuloInicio = novoRotulo();
        String rotuloFim = novoRotulo();

        emitir(rotuloInicio + ":");

        String condicao = visit(ctx.expressao());

        emitir("IF " + condicao + " == 0 GOTO " + rotuloFim);

        visit(ctx.comando());

        emitir("GOTO " + rotuloInicio);
        emitir(rotuloFim + ":");

        return null;
    }

    @Override
    public String visitExpressao(SimpleLangParser.ExpressaoContext ctx) {
        return visit(ctx.expressaoLogica());
    }

    @Override
    public String visitExpressaoLogica(SimpleLangParser.ExpressaoLogicaContext ctx) {
        String esquerda = visit(ctx.expressaoRelacional(0));

        for (int i = 1; i < ctx.expressaoRelacional().size(); i++) {
            String direita = visit(ctx.expressaoRelacional(i));
            String operador = ctx.OPLOG(i - 1).getText().toUpperCase();

            String temporario = novoTemporario();
            emitir(temporario + " = " + esquerda + " " + operador + " " + direita);

            esquerda = temporario;
        }

        return esquerda;
    }

    @Override
    public String visitExpressaoRelacional(SimpleLangParser.ExpressaoRelacionalContext ctx) {
        String esquerda = visit(ctx.expressaoAditiva(0));

        if (ctx.OPREL() == null) {
            return esquerda;
        }

        String direita = visit(ctx.expressaoAditiva(1));
        String operador = ctx.OPREL().getText();

        String temporario = novoTemporario();
        emitir(temporario + " = " + esquerda + " " + operador + " " + direita);

        return temporario;
    }

    @Override
    public String visitExpressaoAditiva(SimpleLangParser.ExpressaoAditivaContext ctx) {
        String esquerda = visit(ctx.expressaoMultiplicativa(0));

        for (int i = 1; i < ctx.expressaoMultiplicativa().size(); i++) {
            String direita = visit(ctx.expressaoMultiplicativa(i));
            String operador = ctx.OPAD(i - 1).getText();

            String temporario = novoTemporario();
            emitir(temporario + " = " + esquerda + " " + operador + " " + direita);

            esquerda = temporario;
        }

        return esquerda;
    }

    @Override
    public String visitExpressaoMultiplicativa(SimpleLangParser.ExpressaoMultiplicativaContext ctx) {
        String esquerda = visit(ctx.expressaoUnaria(0));

        for (int i = 1; i < ctx.expressaoUnaria().size(); i++) {
            String direita = visit(ctx.expressaoUnaria(i));
            String operador = ctx.OPMULT(i - 1).getText();

            String temporario = novoTemporario();
            emitir(temporario + " = " + esquerda + " " + operador + " " + direita);

            esquerda = temporario;
        }

        return esquerda;
    }

    @Override
    public String visitExpressaoUnaria(SimpleLangParser.ExpressaoUnariaContext ctx) {
        if (ctx.OPNEG() != null) {
            String valor = visit(ctx.expressaoUnaria());

            String temporario = novoTemporario();
            emitir(temporario + " = ~" + valor);

            return temporario;
        }

        if (ctx.OPAD() != null) {
            String operador = ctx.OPAD().getText();
            String valor = visit(ctx.expressaoUnaria());

            if (operador.equals("+")) {
                return valor;
            }

            String temporario = novoTemporario();
            emitir(temporario + " = -" + valor);

            return temporario;
        }

        return visit(ctx.expressaoPrimaria());
    }

    @Override
    public String visitExpressaoPrimaria(SimpleLangParser.ExpressaoPrimariaContext ctx) {
        if (ctx.ID() != null) {
            return ctx.ID().getText();
        }

        if (ctx.CTE() != null) {
            return ctx.CTE().getText();
        }

        if (ctx.CADEIA() != null) {
            return ctx.CADEIA().getText();
        }

        if (ctx.TRUE() != null) {
            return "1";
        }

        if (ctx.FALSE() != null) {
            return "0";
        }

        if (ctx.expressao() != null) {
            return visit(ctx.expressao());
        }

        throw new RuntimeException("Erro interno: expressão primária inválida.");
    }
}