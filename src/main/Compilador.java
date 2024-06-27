package main;

import java.io.IOException;
import java.util.List;
import compilador.*;
import util.Util;

public class Compilador {
    public static void main(String[] args) throws IOException, InterruptedException {
        String codigo =
                "string nome = 'Leonardo';"
                + "int valor = 30;"
                + "loop(int i = 0; i < 3;  i+) {"
                + "print i;"
                + "}"
                + "if (valor > 10;) {"
                + "print nome;"
                + "} else {"
                + "print valor;"
                + "}";

        // Geração dos Tokens do analisador lexico
        Lexer lexer = new Lexer(codigo);
        List<Token> tokens = lexer.tokenize();
        Util.ImprimirLista(tokens);

        // Geração da tabela de simbolos em conjunto com o parser
        SymbolTable symbolTable = new SymbolTable();
        Parser parser = new Parser(tokens, symbolTable, "Codigo.java");
        parser.parse();

        System.out.println("Tabela de Símbolos: " + symbolTable);
        
        Util.CompilarCodigoGerado();
    }
}
