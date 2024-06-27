package compilador;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos;
    private Token currentToken;
    private SymbolTable symbolTable;
    private FileWriter fileWriter;

    public Parser(List<Token> tokens, SymbolTable symbolTable, String outputFileName) throws IOException {
        this.tokens = tokens;
        this.pos = 0;
        this.currentToken = tokens.get(pos);
        this.symbolTable = symbolTable;
        this.fileWriter = new FileWriter(outputFileName);
        
        // Escrever o cabeçalho no arquivo
        fileWriter.write("public class Codigo {\n");
        fileWriter.write("    public static void main(String[] args) {\n");
    }

    private void advance() {
        pos++;
        if (pos < tokens.size()) {
            currentToken = tokens.get(pos);
        } else {
            currentToken = null;
        }
    }

    private void eat(TokenType type) {
        if (currentToken.type == type) {
            advance();
        } else {
            throw new RuntimeException("Erro de sintaxe: esperado " + type + " mas encontrado " + currentToken.type);
        }
    }

    public void parse() {
        try {
            while (currentToken != null) {
                if (currentToken.type == TokenType.INT || currentToken.type == TokenType.STRING || currentToken.type == TokenType.FLOAT || currentToken.type == TokenType.CHAR) {
                    parseDeclaracaoVariavel();
                } else if (currentToken.type == TokenType.PRINT) {
                    parsePrint();
                } else if (currentToken.type == TokenType.LOOP) {
                    parseLoop();
                } else if (currentToken.type == TokenType.CLOSEBRACE) {
                    return;
                } else if (currentToken.type == TokenType.IF) {
                    parseIF();
                } else if (currentToken.type == TokenType.ELSE) {
                    parseElse();
                } else {
                    System.out.println("Token não reconhecido");
                    return;
                }
            }
            // Fechar o bloco principal e a classe
            fileWriter.write("    }\n");
            fileWriter.write("}\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    private void parseIF() {
        eat(TokenType.IF);
        eat(TokenType.OPENPARENTESE);
        System.out.println("if (");
        writeToFile("if (");
        parseCondicaoSemPonto();
        eat(TokenType.CLOSEPARENTESE);
        eat(TokenType.OPENBRACE);
        System.out.println(" ){");
        writeToFile(" ){");
        while (currentToken != null && currentToken.type != TokenType.CLOSEBRACE) {
            parse();
        }
        eat(TokenType.CLOSEBRACE);
        System.out.println("}");
        writeToFile("}");
    }

    private void parseElse() {
        eat(TokenType.ELSE);
        eat(TokenType.OPENBRACE);
        System.out.println("else {");
        writeToFile("else {");
        while (currentToken != null && currentToken.type != TokenType.CLOSEBRACE) {
            parse();
        }
        eat(TokenType.CLOSEBRACE);
        System.out.println("}");
        writeToFile("}");
    }

    private void parseLoop() {
        eat(TokenType.LOOP);
        eat(TokenType.OPENPARENTESE);
        System.out.println("for (");
        writeToFile("for (");
        parseDeclaracaoVariavel();
        parseCondicao();
        parseAtualizacao();
        eat(TokenType.CLOSEPARENTESE);
        eat(TokenType.OPENBRACE);
        System.out.println(" ){");
        writeToFile(" ){");
        while (currentToken.type != TokenType.CLOSEBRACE) {
            parse();
        }
        eat(TokenType.CLOSEBRACE);
        System.out.println("}");
        writeToFile("}");
    }

    private void parseCondicao() {
        String var1 = currentToken.value;
        eat(currentToken.type);
        String condicao = currentToken.value;
        eat(currentToken.type);
        String var2 = currentToken.value;
        eat(currentToken.type);
        eat(TokenType.SEMICOLON);
        System.out.println(var1 + " " + condicao + " " + var2 + ";");
        writeToFile(var1 + " " + condicao + " " + var2 + ";");
    }
    private void parseCondicaoSemPonto() {
        String var1 = currentToken.value;
        eat(currentToken.type);
        String condicao = currentToken.value;
        eat(currentToken.type);
        String var2 = currentToken.value;
        eat(currentToken.type);
        eat(TokenType.SEMICOLON);
        System.out.println(var1 + " " + condicao + " " + var2);
        writeToFile(var1 + " " + condicao + " " + var2);
    }

    private void parseAtualizacao() {
        String variavel = currentToken.value;
        eat(TokenType.IDENTIFIER);
        eat(TokenType.INCREMENT);
        System.out.println(variavel + "++");
        writeToFile(variavel + "++");
    }
    
    private void parseDeclaracaoVariavel() {
        TokenType tipo = currentToken.type;
        eat(currentToken.type); // TOKEN STRING, INT, FLOAT, CHAR
        String id = currentToken.value; // NOME DA VARIAVEL
        eat(TokenType.IDENTIFIER); // NOME DA VARIAVEL
        eat(TokenType.ASSIGN); // IGUAL
        Object value = SelectVarType(tipo); // VALOR
        eat(TokenType.SEMICOLON); // PONTO E VIRGULA ENCERRA A DECLARAÇÃO
        
        symbolTable.add(id, value);
        String tipoStr = (tipo == TokenType.STRING) ? "String" : tipo.name().toLowerCase();
        String valueStr = (tipo == TokenType.STRING) ? "\"" + value + "\"" : value.toString();
        System.out.println(tipoStr + " " + id + " = " + valueStr + ";");
        writeToFile(tipoStr + " " + id + " = " + valueStr + ";");
    }    


    private void parsePrint() {
        eat(TokenType.PRINT);
        String id = currentToken.value;
        eat(TokenType.IDENTIFIER);
        eat(TokenType.SEMICOLON);
        Object value = symbolTable.get(id);
        if (value == null) {
            throw new RuntimeException("Variável não declarada: " + id);
        }
        System.out.println("System.out.println(" + id + ");");
        writeToFile("System.out.println(" + id + ");");
    }

    private Object SelectVarType(TokenType tipo) {
        Object value;
        switch (tipo) {
            case INT:
                value = Integer.parseInt(currentToken.value);
                eat(TokenType.NUMINTEGER);
                break;
            case STRING:
                value = "'" + currentToken.value + "'";
                eat(TokenType.TEXTSTRING);
                break;
            case FLOAT:
                value = Float.parseFloat(currentToken.value);
                eat(TokenType.NUMFLOAT);
                break;
            default:
                throw new RuntimeException("Tipo de declaração desconhecido: " + tipo);
        }
        return value;
    }
    
    private void writeToFile(String str) {
        try {
            fileWriter.write(str + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}
