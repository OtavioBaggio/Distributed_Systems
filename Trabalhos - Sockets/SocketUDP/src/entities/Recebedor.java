/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import Interface.JFrame_Cliente;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author Otávio Baggio
 */
public class Recebedor extends Thread{
    
    DatagramSocket socket;
    JFrame_Cliente tela;

    public Recebedor(DatagramSocket socket, JFrame_Cliente tela) {
        this.socket = socket;
        this.tela = tela;
    }

    @Override
    public void run() {
        while (true) {
            //bloqueia aqui, fora da thread da tela
            DatagramPacket pacote = ComunicadorUDP.recebeMensagem(socket);
            if (pacote != null) {
                String mensagem = new String(pacote.getData(), 0, pacote.getLength());
                tela.mostrarMensagem(mensagem);
            }
        }
    }
}
