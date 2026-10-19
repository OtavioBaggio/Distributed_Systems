/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import model.TabelaHash;
import rmi.ConsultaSessao;
import utils.Arquivo;

/**
 *
 * @author Otávio Baggio
 */
public class Server {
    
    // No laboratório, troque localhost pelo IP desta máquina (ex.: rmi://10.104.12.12/ConsultaSessao)
    String HOST_URL = "rmi://192.168.1.240/ConsultaSessao";
    String ARQUIVO = "telephony_sessions.txt";

    public Server() {
        try {
            // 1. abre a base de sessões e adiciona no HashMap
            TabelaHash hashSessoes = new TabelaHash();
            Arquivo.carregarArquivo(hashSessoes.sessoes, ARQUIVO);
            System.out.println("Sessoes carregadas no HashMap... " + hashSessoes.sessoes.size());

            // 2. publica o objeto remoto para os clientes consultarem
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            System.out.println("Porta padrao do RMI Java..." + Registry.REGISTRY_PORT);
            ConsultaSessao objeto_remoto = new ConsultaSessao(hashSessoes);
            System.out.println("Servidor retornando....");
            Naming.rebind(HOST_URL, objeto_remoto);
        } catch (Exception e) {
            System.out.println("Erro de conexão ou " + e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
