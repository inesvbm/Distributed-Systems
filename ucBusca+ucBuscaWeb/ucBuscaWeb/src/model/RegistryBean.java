package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import ucBusca.RMI_interface_server_methods;

public class RegistryBean extends UnicastRemoteObject{
	private static final long serialVersionUID = 1L;
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String username;
	private String password;

	public RegistryBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); 
		}	
	}
	
	/**
	 * 
	 * @return boolean: client is registered
	 * @throws RemoteException
	 */
	public boolean registryBean() throws RemoteException {
		if(server.registry(null, this.username, this.password).compareTo("Registered!")==0) {
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
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}
