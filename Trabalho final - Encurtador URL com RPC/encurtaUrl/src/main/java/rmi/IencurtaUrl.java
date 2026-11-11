package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IencurtaUrl extends Remote {
    
    public String encurtaUrl(String enderecoUrl) throws RemoteException;
    
}
