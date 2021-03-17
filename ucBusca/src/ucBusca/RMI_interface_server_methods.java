package ucBusca;

import java.rmi.*;
/**
 * 
 * @author inesv
 *
 */
public interface RMI_interface_server_methods extends Remote {
	public void client_offline(String username) throws RemoteException;
	public void registry(RMI_interface_client_methods c) throws RemoteException; 
	public void logIn(RMI_interface_client_methods c) throws RemoteException; 
	public void listRegisters(RMI_interface_client_methods c, String username) throws RemoteException;
	public void giveAdminPrivileges(RMI_interface_client_methods c, String username) throws RemoteException;
	public void indexNewURL(RMI_interface_client_methods c, String username) throws RemoteException;
	public void newSearch(RMI_interface_client_methods c, String username) throws RemoteException;
	public void mySearches(RMI_interface_client_methods c, String client_id) throws RemoteException;
	
	public String registryWeb(String nusername, String password) throws RemoteException;
}
