/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import model.Sessao;
import model.TabelaHash;

/**
 *
 * @author Otávio Baggio
 */
public class ConsultaSessao extends UnicastRemoteObject implements IConsultaSessao {

    private TabelaHash hashSessoes;

    public ConsultaSessao(TabelaHash hashSessoes) throws RemoteException {
        this.hashSessoes = hashSessoes;
    }

    @Override
    public String consultaSessao(String numeroCelular) throws RemoteException {
        System.out.println("Numero recebido.... " + numeroCelular);

        Sessao sessao = hashSessoes.sessoes.get(numeroCelular.trim());
        if (sessao == null) {
            System.out.println("   sessao nao encontrada");
            return null;
        }

        System.out.println("   sessao encontrada, enviando ao cliente");
        return sessao.getDadosSessao();
    }

    @Override
    public int totalSessoes() throws RemoteException {
        return hashSessoes.sessoes.size();
    }
    
    
}
