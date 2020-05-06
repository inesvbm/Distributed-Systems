package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import ucBusca.RMI_interface_server_methods;

public class MySearchesBean extends UnicastRemoteObject{
	private static final long serialVersionUID = 1L;
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String username;
	
	public MySearchesBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return boolean: there are searches in client history 
	 * @throws RemoteException
	 */
	public boolean mySearchesBean() throws RemoteException {
		if(server.mySearchesWeb(this.username).size()!=0) {
			return true;
		}
		return false;		
	}
	
	/**
	 * getter/setter
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 
	 * @return historic of client searches
	 * @throws RemoteException
	 */
	public ArrayList<String> getMySearches() throws RemoteException{
		return server.mySearchesWeb(this.username);
	}
}
