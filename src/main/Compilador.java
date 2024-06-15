package main;

import java.util.List;
import compilador.*;

public class Compilador {
    public static void main(String[] args) {
        String codigo = "int x = 10; print x;";

        Lexer lexer = new Lexer(codigo);
        List<Token> tokens = lexer.tokenize();
        System.out.println("Tokens: " + tokens);

        SymbolTable symbolTable = new SymbolTable();
        Parser parser = new Parser(tokens, symbolTable);
        parser.parse();

        System.out.println("Tabela de Símbolos: " + symbolTable);
    }
}
