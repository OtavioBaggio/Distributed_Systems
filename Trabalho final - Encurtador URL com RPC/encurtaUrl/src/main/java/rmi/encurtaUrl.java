package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import model.Url;
import model.TabelaHash;

public class encurtaUrl extends UnicastRemoteObject implements IencurtaUrl {

    private static final String CARACTERES =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private TabelaHash hashUrls;

    public encurtaUrl(TabelaHash hashUrls) throws RemoteException {
        this.hashUrls = hashUrls;
    }

    public String encurtaUrl(String enderecoUrl) throws RemoteException {

        System.out.println("Url recebida.... " + enderecoUrl);

        String endereco = enderecoUrl.trim();
        Url urls = hashUrls.urls.get(endereco);

        if (urls == null) {
            System.out.println("   url nova identificada!");
            System.out.println("   Encurtando a url...");

            // gera um hash que ainda não esteja em uso
            String hash = gerarHash(6);
            while (hashJaExiste(hash)) {
                hash = gerarHash(6);
            }

            // guarda o par no HashMap e devolve o hash gerado
            Url novaUrl = new Url(endereco, hash);
            hashUrls.urls.put(endereco, novaUrl);
            
            return "https://" + hash;
            

        } else {
            System.out.println("   url ja encurtada, devolvendo o hash existente.");
            return "https://" + urls.getUrlEncurtada();
        }
    }

    private String gerarHash(int tamanho) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            sb.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
        }
        return sb.toString();
    }

    private boolean hashJaExiste(String hash) {
        for (Url u : hashUrls.urls.values()) {
            if (u.getUrlEncurtada().equals(hash)) {
                return true;
            }
        }
        return false;
    }
}