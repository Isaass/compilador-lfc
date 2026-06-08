package br.compiladores.codegen;

import br.compiladores.parser.SimpleLangBaseVisitor;
import br.compiladores.parser.SimpleLangParser;

public class GeradorCodigo extends SimpleLangBaseVisitor<Void> {

    private final StringBuilder codigo = new StringBuilder();
    private int contadorRotulos = 0;

    public String getCodigo() {
        return codigo.toString();
    }

    private void emitir(String linha) {
        codigo.append(linha).append(System.lineSeparator());
    }

    private String novoRotulo() {
        return "L" + contadorRotulos++;
    }

    @Override
    public Void visitPrograma(SimpleLangParser.ProgramaContext ctx) {
        emitir("; INICIO DO PROGRAMA");

        if (ctx.declaracoes() != null) {
            visit(ctx.declaracoes());
        }

        visit(ctx.comandoComposto());

        emitir("; FIM DO PROGRAMA");

        return null;
    }

    @Override
    public Void visitDeclaracao(SimpleLangParser.DeclaracaoContext ctx) {
        String tipo = ctx.tipo().getText().toUpperCase();

        for (var tokenId : ctx.listaIds().ID()) {
            String nome = tokenId.getText();
            emitir("DECLARE " + nome + " " + tipo);
        }

        return null;
    }

    @Override
    public Void visitComandoComposto(SimpleLangParser.ComandoCompostoContext ctx) {
        if (ctx.listaComandos() != null) {
            visit(ctx.listaComandos());
        }

        return null;
    }

    @Override
    public Void visitComandoAtribuicao(SimpleLangParser.ComandoAtribuicaoContext ctx) {
        gerarExpressao(ctx.expressao());

        String nomeVariavel = ctx.ID().getText();
        emitir("STORE " + nomeVariavel);

        return null;
    }

    @Override
    public Void visitComandoRead(SimpleLangParser.ComandoReadContext ctx) {
        for (var tokenId : ctx.listaIds().ID()) {
            emitir("READ " + tokenId.getText());
        }

        return null;
    }

    @Override
    public Void visitComandoWrite(SimpleLangParser.ComandoWriteContext ctx) {
        for (var expressao : ctx.listaWrite().expressao()) {
            gerarExpressao(expressao);
            emitir("WRITE_TOP");
        }

        return null;
    }

    @Override
    public Void visitComandoIf(SimpleLangParser.ComandoIfContext ctx) {
        String rotuloElse = novoRotulo();
        String rotuloFim = novoRotulo();

        gerarExpressao(ctx.expressao());

        emitir("JMP_FALSE " + rotuloElse);

        visit(ctx.comando(0));

        if (ctx.comando().size() > 1) {
            emitir("JMP " + rotuloFim);
            emitir(rotuloElse + ":");
            visit(ctx.comando(1));
            emitir(rotuloFim + ":");
        } else {
            emitir(rotuloElse + ":");
        }

        return null;
    }

    @Override
    public Void visitComandoWhile(SimpleLangParser.ComandoWhileContext ctx) {
        String rotuloInicio = novoRotulo();
        String rotuloFim = novoRotulo();

        emitir(rotuloInicio + ":");

        gerarExpressao(ctx.expressao());

        emitir("JMP_FALSE " + rotuloFim);

        visit(ctx.comando());

        emitir("JMP " + rotuloInicio);
        emitir(rotuloFim + ":");

        return null;
    }

    private void gerarExpressao(SimpleLangParser.ExpressaoContext ctx) {
        gerarExpressaoLogica(ctx.expressaoLogica());
    }

    private void gerarExpressaoLogica(SimpleLangParser.ExpressaoLogicaContext ctx) {
        gerarExpressaoRelacional(ctx.expressaoRelacional(0));

        for (int i = 1; i < ctx.expressaoRelacional().size(); i++) {
            gerarExpressaoRelacional(ctx.expressaoRelacional(i));

            String operador = ctx.OPLOG(i - 1).getText().toUpperCase();

            if (operador.equals("AND")) {
                emitir("AND");
            } else if (operador.equals("OR")) {
                emitir("OR");
            }
        }
    }

    private void gerarExpressaoRelacional(SimpleLangParser.ExpressaoRelacionalContext ctx) {
        gerarExpressaoAditiva(ctx.expressaoAditiva(0));

        if (ctx.OPREL() != null) {
            gerarExpressaoAditiva(ctx.expressaoAditiva(1));

            String operador = ctx.OPREL().getText();

            switch (operador) {
                case ">":
                    emitir("GT");
                    break;
                case "<":
                    emitir("LT");
                    break;
                case ">=":
                    emitir("GE");
                    break;
                case "<=":
                    emitir("LE");
                    break;
                case "==":
                    emitir("EQ");
                    break;
                case "<>":
                    emitir("NE");
                    break;
                default:
                    throw new RuntimeException("Operador relacional inválido: " + operador);
            }
        }
    }

    private void gerarExpressaoAditiva(SimpleLangParser.ExpressaoAditivaContext ctx) {
        gerarExpressaoMultiplicativa(ctx.expressaoMultiplicativa(0));

        for (int i = 1; i < ctx.expressaoMultiplicativa().size(); i++) {
            gerarExpressaoMultiplicativa(ctx.expressaoMultiplicativa(i));

            String operador = ctx.OPAD(i - 1).getText();

            if (operador.equals("+")) {
                emitir("ADD");
            } else if (operador.equals("-")) {
                emitir("SUB");
            }
        }
    }

    private void gerarExpressaoMultiplicativa(SimpleLangParser.ExpressaoMultiplicativaContext ctx) {
        gerarExpressaoUnaria(ctx.expressaoUnaria(0));

        for (int i = 1; i < ctx.expressaoUnaria().size(); i++) {
            gerarExpressaoUnaria(ctx.expressaoUnaria(i));

            String operador = ctx.OPMULT(i - 1).getText();

            if (operador.equals("*")) {
                emitir("MUL");
            } else if (operador.equals("/")) {
                emitir("DIV");
            }
        }
    }

    private void gerarExpressaoUnaria(SimpleLangParser.ExpressaoUnariaContext ctx) {
        if (ctx.OPNEG() != null) {
            gerarExpressaoUnaria(ctx.expressaoUnaria());
            emitir("NOT");
            return;
        }

        if (ctx.OPAD() != null) {
            String operador = ctx.OPAD().getText();

            gerarExpressaoUnaria(ctx.expressaoUnaria());

            if (operador.equals("-")) {
                emitir("NEG");
            }

            return;
        }

        gerarExpressaoPrimaria(ctx.expressaoPrimaria());
    }

    private void gerarExpressaoPrimaria(SimpleLangParser.ExpressaoPrimariaContext ctx) {
        if (ctx.ID() != null) {
            emitir("LOAD " + ctx.ID().getText());
            return;
        }

        if (ctx.CTE() != null) {
            emitir("PUSH " + ctx.CTE().getText());
            return;
        }

        if (ctx.CADEIA() != null) {
            emitir("PUSH_STRING " + ctx.CADEIA().getText());
            return;
        }

        if (ctx.TRUE() != null) {
            emitir("PUSH_TRUE");
            return;
        }

        if (ctx.FALSE() != null) {
            emitir("PUSH_FALSE");
            return;
        }

        if (ctx.expressao() != null) {
            gerarExpressao(ctx.expressao());
        }
    }
}
