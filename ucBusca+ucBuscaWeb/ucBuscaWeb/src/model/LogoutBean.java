package model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import ucBusca.RMI_interface_server_methods;

public class LogoutBean {
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String username;
	private String password;
	
	public LogoutBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * @return boolean: client is logged out
	 * @throws RemoteException
	 */
	public boolean logoutBean() throws RemoteException {
		if(server.logOut(null, this.username, this.password).equals("Logout!")) {
			return true;
		}
		return false;		
	}
	
	/**
	 * getter/setter
	 * @return username
	 */
	public String getUsername(){
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
