package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import ucBusca.RMI_interface_server_methods;

public class AdminPrivBean extends UnicastRemoteObject{
	private static final long serialVersionUID = 1L;
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String username;
	private String clientUsername;
	
	public AdminPrivBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * @return client is/is not admin
	 * @throws RemoteException
	 */
	public boolean adminPrivileges() throws RemoteException {
		if(server.giveAdminPrivileges(null, this.username, this.clientUsername).contains("_is_ADMIN!")) {
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
	 * getter/setter
	 * @return clientUsername
	 */
	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
}
