package ucBusca;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.io.*;

/**
 * 
 * @author inesv
 *
 */
public class RMIClient extends UnicastRemoteObject implements RMI_interface_client_methods {
	public static final long serialVersionUID = 1L;
	public static int PORT_RMI=5614;
	static String username="";
	static boolean rmiServer_up=false;

	RMIClient() throws RemoteException {
		super();	
	}
	
	public void rmi_Up() throws RemoteException {		
		rmiServer_up=true;
	}
	
	public void print_on_client(String s) throws RemoteException {		
		System.out.println(s);
	}
	
	public void client_username(String s) throws RemoteException {
		username=s;
	}

	/**
	 * remote method that asks client for data (depending on the command he selected)
	 */
	public String ask_client(String s) throws RemoteException {
		String data="";
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		System.out.println(s);
		try{				
			while(data.compareTo("")==0){
				data=reader.readLine();				
				if(data.compareTo("")!=0){
					return data;
				}
				System.out.println("You must insert data!");
			}
		} catch(IOException e){
			System.out.println("Error!");
		}
		return data;
	}
	
	public static void main(String args[]) {		
		String command="";		
		//generate id for clients who are not logged		
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		RMI_interface_server_methods h;
		RMIClient c;		
		System.out.println("----------------RMI Client ready----------------");
		while(true){
			try {
				h = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup(args[0]);
				c = new RMIClient();
				while (true) {	
					System.out.println("INSERT COMMAND");		
					command = reader.readLine();
					if(command!=null){					
						switch(command){
							case "registry":	
								h.registry(c);
								break;
							case "login":
								h.logIn(c);
								break;
							case "registers_list":
								h.listRegisters(c, username);
								break;
							case "admin_privileges":
								h.giveAdminPrivileges(c, username);
								break;
							case "index_url":
								h.indexNewURL(c, username);
								break;
							case "search":
								h.newSearch(c, username);
								break;
							case "my_searches":
								h.mySearches(c, username);
								break;
							case "close":
								System.exit(0);
							default:
	                            System.out.println("Wrong command!");
	                            break; 
						}	
					}
					else{
				        h.client_offline(username);								
						System.out.println("Offline client");
						break;
					}
					
				}	
			} catch (IOException e) {	
				//if RMI server goes down, client doesn't figure it out
				try{
					while(true){
						h = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup(args[0]);
						c = new RMIClient();
						break;
					} 					
				} catch(IOException i){					
					System.out.println("Close Client and try again later"); 
					break;
				} catch (NotBoundException n){
					System.out.println("NotBoundException");					
				} 
			} catch (NotBoundException n){
				System.out.println("NotBoundException");
			} 
		}
	}
}

