/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import model.Sessao;

/**
 *
 * @author Otávio Baggio
 */
public class Arquivo {
    
    public static void carregarArquivo(HashMap<String, Sessao> sessoes, String caminho) throws IOException {
        List<String> linhas = Files.readAllLines(Paths.get(caminho), StandardCharsets.UTF_8);

        for (String linha : linhas) {
            if (linha.trim().isEmpty()) {
                continue;
            }
            // limite 2: separa só no primeiro ';', o JSON fica inteiro
            String[] partes = linha.split(";", 2);
            if (partes.length == 2) {
                Sessao sessao = new Sessao(partes[0].trim(), partes[1].trim());
                sessoes.put(sessao.getNumeroCelular(), sessao);
            }
        }
    }
}
