package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import ucBusca.RMI_interface_server_methods;

public class NotificationsBean extends UnicastRemoteObject{
	private static final long serialVersionUID = 1L;
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String username;
	private String message;
	
	public NotificationsBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); 
		}	
	}
	
	/**
	 *  
	 * @return boolean: client has a notification
	 * @throws RemoteException
	 */
	public boolean notificationsBean() throws RemoteException {
		if(server.getOfflineMsgs(this.username)!=null) {
			String aux=server.getOfflineMsgs(this.username);
			this.setMessage(aux);
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
	 * @return notification for client
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
