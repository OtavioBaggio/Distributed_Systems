/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 *
 * @author Otávio Baggio
 */
public class ServidorToken {
    DatagramSocket socket;
    ArrayList<Usuario> lista = new ArrayList<>();
    
    public ServidorToken() {
        criaServerSocket();
        System.out.println("Servidor ativo na porta 1234");

        while (true) {
            DatagramPacket pacote = ComunicadorUDP.recebeMensagem(socket);
            String mensagem = new String(pacote.getData(), 0, pacote.getLength());
            String ip = pacote.getAddress().getHostAddress();
            int porta = pacote.getPort();
            System.out.println("Recebi " + mensagem + " de " + ip + ":" + porta);

            String partes[] = mensagem.split(";");
            if (partes[0].equals("CADASTRO")) {
                cadastrar(partes[1], partes[2], ip, porta);
            } else if (partes[0].equals("TOKEN")) {
                solicitarToken(partes[1], ip, porta);
            }
        }
    }

    public synchronized void cadastrar(String nome, String email, String ip, int porta) {
        Usuario u = new Usuario(nome, email);
        if (lista.contains(u)) {
            responder("Usuário já cadastrado: " + email, ip, porta);
        } else {
            lista.add(u);
            responder("Usuário cadastrado: " + email, ip, porta);
        }
    }

    public synchronized void solicitarToken(String email, String ip, int porta) {
        int posicao = lista.indexOf(new Usuario("", email));
        if (posicao == -1) {
            responder("Email não cadastrado: " + email, ip, porta);
        } else {
            responder("Iniciando envio de tokens para " + email, ip, porta);
            //uma thread por usuário
            new GeradorToken(lista.get(posicao), socket, ip, porta).start();
        }
    }

    public void responder(String mensagem, String ip, int porta) {
        DatagramPacket pacote = ComunicadorUDP.montaMensagem(mensagem, ip, porta);
        ComunicadorUDP.enviaMensagem(socket, pacote);
    }

    private void criaServerSocket() {
        try {
            socket = new DatagramSocket(1234);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }
    
}
