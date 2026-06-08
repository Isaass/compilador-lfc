// Generated from SimpleLang.g4 by ANTLR 4.13.2
package br.compiladores.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SimpleLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SimpleLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(SimpleLangParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#declaracoes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracoes(SimpleLangParser.DeclaracoesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#listaDeclaracoes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaDeclaracoes(SimpleLangParser.ListaDeclaracoesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#declaracao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao(SimpleLangParser.DeclaracaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#listaIds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaIds(SimpleLangParser.ListaIdsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(SimpleLangParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comandoComposto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoComposto(SimpleLangParser.ComandoCompostoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#listaComandos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaComandos(SimpleLangParser.ListaComandosContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comando}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComando(SimpleLangParser.ComandoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comandoIf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoIf(SimpleLangParser.ComandoIfContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comandoWhile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoWhile(SimpleLangParser.ComandoWhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comandoRead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoRead(SimpleLangParser.ComandoReadContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comandoWrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoWrite(SimpleLangParser.ComandoWriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#listaWrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaWrite(SimpleLangParser.ListaWriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#comandoAtribuicao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoAtribuicao(SimpleLangParser.ComandoAtribuicaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressao(SimpleLangParser.ExpressaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressaoLogica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressaoLogica(SimpleLangParser.ExpressaoLogicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressaoRelacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressaoRelacional(SimpleLangParser.ExpressaoRelacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressaoAditiva}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressaoAditiva(SimpleLangParser.ExpressaoAditivaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressaoMultiplicativa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressaoMultiplicativa(SimpleLangParser.ExpressaoMultiplicativaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressaoUnaria}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressaoUnaria(SimpleLangParser.ExpressaoUnariaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expressaoPrimaria}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressaoPrimaria(SimpleLangParser.ExpressaoPrimariaContext ctx);
}