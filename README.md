# Compilador LFC

## Requisitos

Para executar o projeto, é necessário ter instalado:

* Java 17
* Maven

Para verificar se o Java está instalado:

```bash
java -version
```

Para verificar se o Maven está instalado:

```bash
mvn -version
```
## Como compilar o projeto

Na raiz do projeto, execute:

```bash
mvn clean compile
```

Esse comando limpa arquivos gerados anteriormente, gera novamente as classes do ANTLR4 e compila o projeto Java.

## Como executar o programa

Após compilar o projeto, execute um arquivo de exemplo usando:

```bash
mvn exec:java "-Dexec.args=exemplos/valido.txt"
```

O caminho passado em `-Dexec.args` indica qual arquivo da pasta `exemplos` será analisado pelo compilador.

## Executar programa válido simples

```bash
mvn exec:java "-Dexec.args=exemplos/valido.txt"
```

Esse exemplo demonstra:

* declaração de variáveis;
* comando READ;
* comando WRITE;
* atribuições;
* análise léxica;
* análise sintática;
* análise semântica;
* geração de código intermediário.

## Executar programa válido completo

```bash
mvn exec:java "-Dexec.args=exemplos/valido-completo.txt"
```

Esse exemplo demonstra:

* expressões aritméticas;
* precedência de operadores;
* expressão relacional;
* comando IF/ELSE;
* comando WHILE;
* geração de rótulos no código intermediário.

## Executar exemplo com erro léxico

```bash
mvn exec:java "-Dexec.args=exemplos/erro-lexico.txt"
```

Esse exemplo demonstra erro léxico, como constante inteira acima do limite permitido.

## Executar exemplo com erro sintático

```bash
mvn exec:java "-Dexec.args=exemplos/erro-sintatico.txt"
```

Esse exemplo demonstra erro sintático, como ausência de ponto e vírgula em uma posição obrigatória.

## Executar exemplo com erro semântico por tipo incompatível

```bash
mvn exec:java "-Dexec.args=exemplos/erro-semantico-tipo.txt"
```

Esse exemplo demonstra erro semântico quando uma variável recebe um valor de tipo incompatível.

## Executar exemplo com erro semântico por variável não declarada

```bash
mvn exec:java "-Dexec.args=exemplos/erro-semantico-nao-declarada.txt"
```

Esse exemplo demonstra erro semântico quando uma variável é usada sem ter sido declarada.

## Executar exemplo com erro semântico por variável duplicada

```bash
mvn exec:java "-Dexec.args=exemplos/erro-semantico-duplicada.txt"
```

Esse exemplo demonstra erro semântico quando uma variável é declarada mais de uma vez.

## Executar exemplo com erro semântico em condição de IF

```bash
mvn exec:java "-Dexec.args=exemplos/erro-semantico-if.txt"
```

Esse exemplo demonstra erro semântico quando a condição do IF não é booleana.