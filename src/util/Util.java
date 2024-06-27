package util;

import java.io.IOException;
import java.util.List;
import compilador.Token;

public class Util {
	public static void ImprimirLista(List<Token> tokens) {
		System.out.println("*** Lista de Tokens Reconhecidos ***");
        for (Token token : tokens) {
			System.out.println(token);
		}
        System.out.println("*** Fim da Lista de Tokens Reconhecidos ***");
        System.out.println();
        System.out.println();
	}
	
	public static void CompilarCodigoGerado() throws IOException, InterruptedException {
		System.out.println("\n\n*** Rodando Compilação do Código Gerado ***");
        // Compilar o arquivo Codigo.java
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "javac Codigo.java");
        Process compileProcess = compileProcessBuilder.start();
        compileProcess.waitFor();
        
        System.out.println("*** Executando o Código ***");
        if (compileProcess.exitValue() == 0) {
            ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "java Codigo");
            runProcessBuilder.inheritIO();
            Process runProcess = runProcessBuilder.start();
            runProcess.waitFor();
        } else {
            System.out.println("Erro ao compilar Codigo.java");
        }
	}
}
