package ucBusca;

import java.rmi.*;

/**
 * 
 * @author inesv
 *
 */
public interface RMI_interface_client_methods extends Remote {
	public String ask_client(String s) throws java.rmi.RemoteException;
	public void print_on_client(String s) throws java.rmi.RemoteException;
	public void client_username(String s) throws RemoteException;

}
