package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import ucBusca.RMI_interface_server_methods;

public class URLBean extends UnicastRemoteObject {
	private static final long serialVersionUID = 1L;
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String username;
	private String url;

	public URLBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * @return boolean: URL was indexed
	 * @throws RemoteException
	 */
	public boolean urlBean() throws RemoteException {
		if(server.indexNewURL(null, this.username, this.url).equals("URLIndex_indexed!")) {
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
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
