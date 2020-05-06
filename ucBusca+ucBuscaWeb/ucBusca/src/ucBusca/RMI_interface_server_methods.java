package ucBusca;

import java.rmi.*;
import java.util.ArrayList;
/**
 * 
 * @author inesv
 *
 */
public interface RMI_interface_server_methods extends Remote {
	public void client_offline(String username) throws RemoteException;
	public String registry(RMI_interface_client_methods c, String user, String pass) throws RemoteException;
	public String logIn(RMI_interface_client_methods c, String user, String pass) throws RemoteException;
	public void listRegisters(RMI_interface_client_methods c, String username) throws RemoteException;
	public String giveAdminPrivileges(RMI_interface_client_methods c, String username, String clientUsername) throws RemoteException;
	public String indexNewURL(RMI_interface_client_methods c, String username, String website) throws RemoteException;
	public void newSearch(RMI_interface_client_methods c, String username) throws RemoteException;
	public void mySearches(RMI_interface_client_methods c, String client_id) throws RemoteException;
	public String logOut(RMI_interface_client_methods c, String user, String pass) throws RemoteException;
	
	public String getOfflineMsgs(String username) throws RemoteException;
	public ArrayList<ArrayList<String>> newSearchWeb(String username, String text) throws RemoteException;
	public ArrayList<String> mySearchesWeb(String username) throws RemoteException;
		
}
