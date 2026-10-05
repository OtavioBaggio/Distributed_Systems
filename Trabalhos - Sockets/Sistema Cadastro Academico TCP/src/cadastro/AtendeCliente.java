/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Otávio Baggio
 */
public class AtendeCliente extends Thread{
    
    Socket cliente;
    JFrame_Servidor servidor;

    public AtendeCliente(Socket cliente, JFrame_Servidor servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }
    
    
    public void run(){
        try {
            //receber a pessoa do cliente
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            Pessoa p = (Pessoa) entrada.readObject();
            servidor.log("Recebido: " + p.getNome() + " - " + p.getDataNascimento());

            //cadastrar (ou localizar) na lista
            Pessoa resposta = servidor.cadastrar(p);

            //devolver o objeto para o cliente
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(resposta);

            saida.close();
            entrada.close();
            cliente.close();
        } catch (IOException | ClassNotFoundException e) {
            servidor.log("Erro: " + e.getMessage());
        }
    }
    
}
