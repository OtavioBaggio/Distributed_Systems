/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

/**
 *
 * @author Otávio Baggio
 */
public class GeradorToken extends Thread{
    
    Usuario usuario;
    DatagramSocket socket;
    String ip;
    int porta;
    int intervalo = 5000; //X segundos (5s)

    public GeradorToken(Usuario usuario, DatagramSocket socket, String ip, int porta) {
        this.usuario = usuario;
        this.socket = socket;
        this.ip = ip;
        this.porta = porta;
    }

    @Override
    public void run() {
        while (true) {
            //gera um token aleatório de 8 caracteres
            String token = UUID.randomUUID().toString().substring(0, 8);
            String mensagem = "TOKEN para " + usuario.getEmail() + ": " + token;

            DatagramPacket pacote = ComunicadorUDP.montaMensagem(mensagem, ip, porta);
            ComunicadorUDP.enviaMensagem(socket, pacote);
            System.out.println("Enviado -> " + mensagem);

            try {
                Thread.sleep(intervalo);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
