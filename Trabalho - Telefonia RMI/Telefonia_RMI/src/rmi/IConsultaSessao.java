/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Otávio Baggio
 */
public interface IConsultaSessao extends Remote{
    
    public String consultaSessao(String numeroCelular) throws RemoteException;
    public int totalSessoes() throws RemoteException;
    
}
