package ucBusca;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface RMI_interface_server_methods extends Remote {
	
	public String getOfflineMsgs(String username) throws RemoteException;
	public String indexNewURL(RMI_interface_client_methods c, String username, String website) throws RemoteException;
	public ArrayList<ArrayList<String>> newSearchWeb(String username, String text) throws RemoteException;
	public ArrayList<String> mySearchesWeb(String username) throws RemoteException;
	public String logOut(RMI_interface_client_methods c, String user, String pass) throws RemoteException;
	public String giveAdminPrivileges(RMI_interface_client_methods c, String username, String clientUsername) throws RemoteException;
	
	public String registry(RMI_interface_client_methods c, String user, String pass) throws RemoteException;
	public String logIn(RMI_interface_client_methods c, String user, String pass) throws RemoteException;
}
