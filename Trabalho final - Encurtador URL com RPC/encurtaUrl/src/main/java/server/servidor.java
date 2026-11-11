package server;

import java.rmi.*;
import java.rmi.registry.*;
import java.util.HashMap;
import model.TabelaHash;
import model.Url;
import rmi.IencurtaUrl;
import rmi.encurtaUrl;

public class servidor {

    /* Endereço usado para registrar o serviço remoto */
    String HOST_URL = "rmi://localhost/EncurtaUrl";

    public servidor() {
        try {

            TabelaHash tabela = new TabelaHash();
            
            /* Inicia o registro RMI, que permite localizar os serviços remotos */
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            /* Cria o objeto remoto responsável pelo encurtamento das URLs */
            IencurtaUrl objetoRemoto = new encurtaUrl(tabela);

            /* Registra o objeto remoto para que os clientes possam acessá-lo */
            Naming.bind(HOST_URL, objetoRemoto);

            System.out.println("Servidor online e à espera de clientes...");

        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }

    public static void main(String args[]) {
        new servidor();
    }
}