package ucBusca;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/**
 * 
 * @author inesv
 *
 */
public class RMIServer extends UnicastRemoteObject implements RMI_interface_server_methods {
	private static final long serialVersionUID = 1L;
	private static String MULTICAST_ADDRESS = "224.3.2.1";
	public static int PORT_RMI=5614;
	public static int PORT_MULT = 7813;
	//store client requests and a client interface
	public ArrayList<ClientRequest> requests=new ArrayList<ClientRequest>();
	//store multicast info, to send to the ADMIN page
	public ArrayList<MulticastData> multicastData=new ArrayList<MulticastData>();
	//store messages from a client when he is offline
	private HashMap<String, String> offlineMsgs=new HashMap<String, String>();	
	public HashSet<String> admins=new HashSet<String>();
	
	public String registryRes;
	public String loginRes="";
	public String logoutRes;
	public String urls;
	public String newAdmin;
	public String notification;
	public ArrayList<ArrayList<String>> searchR;
	public ArrayList<String> historic;
	public String user;

	public RMIServer() throws RemoteException {
		super();							
	}
	
	/////////////////////////////////////////////////////////////////////////////////// WEB ///////////////////////////////////////////////////////////
	
	public String getOfflineMsgs(String username) throws RemoteException {
		return this.notification;
	}
	
	public ArrayList<ArrayList<String>> newSearchWeb(String username, String text) throws RemoteException {
		adminPageOff(username);
		sendToMulticast("search", text, username, null);
		return this.searchR;
	}
	
	public ArrayList<String> mySearchesWeb(String username) throws RemoteException{
		adminPageOff(username);
		sendToMulticast("my_searches", username, "", null);
		return this.historic;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void client_offline(String username) throws RemoteException {
		sendToMulticast("offline", username, "", null);
	}
	
	public void adminPageOff(String username){
		if(admins.contains(username))
			admins.remove(username);
	}

	/**
	 * client wants to register
	 */
	public String registry(RMI_interface_client_methods c, String user, String pass) throws RemoteException {		
		if(c!=null) {			
			String username=c.ask_client("Insert username:");
			String password=c.ask_client("Insert password:");
			adminPageOff(username);
			sendToMulticast("registry", username, password, c);	
		}
		else {
			adminPageOff(user);
			sendToMulticast("registry", user, pass, null);
		}		
		return this.registryRes; 
	}
	
	/**
	 * client wants to log in
	 */
	public String logIn(RMI_interface_client_methods c, String user, String pass) throws RemoteException {
		if(c!=null) {
			String username=c.ask_client("Insert username:");
			String password=c.ask_client("Insert password:");
			adminPageOff(username);
			sendToMulticast("login", username, password, c);
		}
		else {
			adminPageOff(user);
			this.user=user;
			sendToMulticast("login", user, pass, null);				
		}
		return this.loginRes;
	}
	
	/**
	 * client wants to log out
	 */
	public String logOut(RMI_interface_client_methods c, String user, String pass) throws RemoteException {
		if(c!=null) {
			String username=c.ask_client("Insert username:");
			String password=c.ask_client("Insert password:");
			sendToMulticast("logout", username, password, c);	
		}
		else {
			adminPageOff(user);
			sendToMulticast("logout", user, pass, null);				
		}
		return this.logoutRes;
	}
	
	/**
	 * ADMIN wants to see the system info
	 */
	public void listRegisters(RMI_interface_client_methods c, String username) throws RemoteException {
		sendToMulticast("registers_list", username, "", c);
	}
	
	/**
	 * ADMIN wants to select another user to give him ADMIN privileges
	 */	
	public String giveAdminPrivileges(RMI_interface_client_methods c, String username, String clientUsername) throws RemoteException {
		if(c!=null) {
			adminPageOff(username);
			String clientUser=c.ask_client("Choose a username:");
			sendToMulticast("admin_privileges", username, clientUser, c);
		}
		else {
			adminPageOff(username);
			sendToMulticast("admin_privileges", username, clientUsername, null);
		}
		return this.newAdmin;
	}
	
	/**
	 * ADMIN wants to index a new URL
	 */
	public String indexNewURL(RMI_interface_client_methods c, String username, String website) throws RemoteException{
		if(c!=null) {
			adminPageOff(username);
			String web=c.ask_client("Choose a URL to index");
			sendToMulticast("index_url", username, web, c);
		}
		else {
			adminPageOff(username);
			sendToMulticast("index_url", username, website, null);			
		}
		return this.urls;
	}
	
	/**
	 * user wants to make a new search
	 */
	public void newSearch(RMI_interface_client_methods c, String username) throws RemoteException{
		adminPageOff(username);
		String text=c.ask_client("Search:");
		sendToMulticast("search", text, username, c);
	}
	
	/**
	 * logged client wants to see the historic of his searches
	 */
	public void mySearches(RMI_interface_client_methods c, String username) throws RemoteException{
		adminPageOff(username);
		if(this.user==null) {
			sendToMulticast("my_searches", username, "", c);
		}
		else {
			sendToMulticast("my_searches", this.user, "", c);
		}
	}

	//handle response from Multicast server
	/**
	 * this method makes the distinction between messages received from Multicast Server 
	 * and calls the corresponding method to handle each one
	 * @param message: received from Multicast server
	 * @param c: client intrerface
	 * @param client_id
	 */
	public void handleMulticastResponse(String message, RMI_interface_client_methods c, String username){
		HashMap<String, String> data=new HashMap<String, String>();
		message=message.replaceAll(" \\| ", "|");
		message=message.replaceAll(" ; ", ";");
		String[] pairsKV=message.split(";");	
		String[] keyValue;
		for(int i=0;i<pairsKV.length;i++){	
			keyValue=pairsKV[i].split("\\|");
			data.put(keyValue[0], keyValue[1]);
		}			
		//handle registry			
		if(data.containsKey("registered")){
			register_Handler(data);
		}
		//handle login	
		else if(data.containsKey("logged")){
			login_Handler(data);
		}		
		//handle logout	
		else if(data.containsValue("Logout!")){
			logout_Handler(data);
		}
		//registers list
		else if(data.containsValue("requests_list_allowed") || data.containsValue("registersList_Not_Admin!") || data.containsValue("registersList_Not_logged!")){
			registersList_Handler(data);
		}
		//admin privileges
		else if(data.containsValue("admin_privileges")){
			adminPrivileges_handler(data);
		}
		//URL index
		else if(data.containsValue("URLIndex_indexed!") || data.containsValue("URLIndex_Must_be_ADMIN!") || data.containsValue("URLIndex_Must_be_logged!")){
			URLIndex_handler(data);
		}
		//search
		else if((data.containsValue("search_results") && data.containsKey("item_count")) || data.containsValue("searchText_Not_found!")){			
			search_handler(data);
		}
		//my searches
		else if((data.containsValue("yourSearches") && data.containsKey("item_count")) || data.containsValue("yourSearches_Not_found!") || data.containsValue("yourSearches_Must_be_registered_logged!")){			
			mySearches_handler(data);
		}
	}
	
	
	
	/**
	 * handles the registry response from Multicast server to a client
	 * @param data: message data from Multicast server
	 */
	public void register_Handler(HashMap<String, String> data){		
		String res=data.get("msg");
		this.registryRes=res;
		checkRequestID(res, data);		
	}

	/**
	 * handles the login response from Multicast server to a client.
	 * when a client logs in, he receives a message saying he is logged and his
	 * offline messages are displayed
	 * @param data: message data from Multicast server
	 */
	public void login_Handler(HashMap<String, String> data){
		String res=data.get("msg");
		String request_id=data.get("request_ID");				
		String admin=data.get("admin");
		
		this.loginRes=res+"----"+admin;
		
		for(ClientRequest r: requests){						
			if(r.request_id.compareTo(request_id)==0){				
				if(res.compareTo("Logged!")==0){
					r.logged=1;
					if(r.client!=null) {
						try{
							r.client.print_on_client(res);
							r.client.client_username(r.username);
							for(HashMap.Entry<String,String> entry : offlineMsgs.entrySet()){
								if(entry.getKey().compareTo(r.username)==0){
									r.client.print_on_client(entry.getValue());
								}
							}
						} catch(RemoteException e){
							System.out.println("Could not send message to client");
						}
					}
					else{
						try{
							r.client.print_on_client(res);
						} catch(RemoteException e){
							System.out.println("Could not send message to client");
						}					
					}
				}
			}			
		}
	}
	
	
	/**
	 * handles the logout response from Multicast server to a client.
	 * when a client logs out, he receives a message saying he is logged off
	 * @param data: message data from Multicast server
	 */
	public void logout_Handler(HashMap<String, String> data){
		String res=data.get("msg");
		this.logoutRes=res;
		checkRequestID(res, data);	
	}
	
	
	/**
	 * handles the ADMIN page response, received from Multicast server
	 * displays all system info to the ADMIN 
	 * @param data: message data from Multicast server
	 */
	public void registersList_Handler(HashMap<String, String> data){
		String res="";
		if(data.containsValue("requests_list_allowed")) {
			String username=data.get("username");
			admins.add(username);			
		}
		else {
			res=data.get("msg");
			checkRequestID(res, data);
		}
	}
	
	/**
	 * handles the ADMIN privileges response, received from Multicast server
	 * @param data: message data from Multicast server
	 */
	public void adminPrivileges_handler(HashMap<String, String> data){	
		String usernameDest=data.get("usernameDest");
		String res="";
		String res1="";
		res=data.get("msg");	
		
		//notification to admin who made the request
		checkRequestID(res, data);
		
		//notification to client
		if(res.contains("is_ADMIN!")){
			res1="Notification from ADMIN: You have received ADMIN privileges!";
			offlineMsgs.put(usernameDest, res1);
			for(ClientRequest r: requests){
				//if client is logged, receives notification in the moment 				
				if(r.username.compareTo(usernameDest)==0 && r.logged==1){						
					if(r.client!=null) {
						try{	
							r.client.print_on_client(res1);
							break;						
						} catch(RemoteException e){
							System.out.println("Could not deliver message");
						}					
					}
				}
				//if client is offline, the notification is stored in the offline messages list 
				else if(r.username.compareTo(usernameDest)==0 && r.logged==0){
					offlineMsgs.put(usernameDest, res1);						
				}				
			}			
			this.notification=usernameDest+"----"+res1;
		}	
		this.newAdmin=res;
	}
	
	/**
	 * handles the URL index response, received from Multicast server
	 * @param data: message data from Multicast server
	 */
	public void URLIndex_handler(HashMap<String, String> data){
		String res=data.get("msg");
		this.urls=res;
		checkRequestID(res, data);	
		//String[] d=data.get("Important_Urls").split("----");
		
		/*for(ClientRequest c: requests) {
			if(admins.contains(c.username)) {
				try{
					c.client.print_on_client("IMPORTANT SITES");
					for(int i=0;i<d.length;i++) {
						c.client.print_on_client(d[i]);							
					}
					break;
				} catch(RemoteException e){
					System.out.println("Could not send message to client");
				}						
			}
		}*/
	}
	
	/**
	 * handles the search response, received from Multicast server
	 * displays the result of a search, showing title, description, main URL, number 
	 * of connections to the main URL and all those connections displayed
	 * @param data: message data from Multicast server
	 */
	public void search_handler(HashMap<String, String> data){
		String res="";
		
		if(!data.containsKey("msg")) {
		
			ArrayList<String> aux=new ArrayList<String>();
			ArrayList<PageData> aux1=new ArrayList<PageData>();
			
			ArrayList<ArrayList<String>> aux2=new ArrayList<>();
			
			/*String[] d=data.get("Common_searches").split("----");
	
			for(ClientRequest c: requests) {
				if(admins.contains(c.username)) {
					try{
						c.client.print_on_client("COMMON SEARCHES");
						for(int i=0;i<d.length;i++) {
							c.client.print_on_client(d[i]);							
						}
						break;
					} catch(RemoteException e){
						System.out.println("Could not send message to client");
					}						
				}
			}*/
			
			for(HashMap.Entry<String,String> entry : data.entrySet()){
				if(entry.getKey().contains("item_") && entry.getKey().compareTo("item_count")!=0){
					aux.add(entry.getValue());		
				}
				else if(entry.getKey().contains("msg")){
					res=data.get("msg");
				}
			}
			
			for(String f: aux){			
				String[] s=f.split("----");	
				if(s.length>=4) {
					int num=Integer.parseInt(s[3]);
					PageData p=new PageData(s[0], s[1], s[2], num);
					if(s.length>4){
						for(int j=4;j<s.length;j++){
							p.connections_to.add(s[j]);
						}
					}
					aux1.add(p);
				}
			}
	
			//sort results by number of connections
			Collections.sort(aux1, new Comparator<PageData>() {
			    @Override
			    public int compare(PageData o1, PageData o2) {	    	
			        return o2.count_links-o1.count_links;
			    }
			});
			
			res+="\n";
			for(PageData p: aux1) {
				res+=p.title+"\n"+p.description+"\n"+p.name+"\n"+p.count_links+" connections:\n";
				for(String s: p.connections_to) {
					res+=s+"\n";
				}
				res+="\n";
			}
			
			for(PageData p: aux1) {
				ArrayList<String> a=new ArrayList<>();
				a.add(p.title);
				a.add(p.description);
				a.add(p.name);
				a.add(Integer.toString(p.count_links)+" connections:");
				for(String s: p.connections_to) {
					a.add(s);
				}
				aux2.add(a);
			}
			this.searchR=aux2;
		}
		else {
			res="Search not found!";
			this.searchR=new ArrayList<ArrayList<String>>();
		}
		checkRequestID(res, data);	
	}
	
	/**
	 * handles the historic of searches response, received from Multicast server
	 * displays all searches made by the client. Most recent searches are displayed first
	 * @param data: message data from Multicast server
	 */
	public void mySearches_handler(HashMap<String, String> data){
		String res="";
		ArrayList<String> aux=new ArrayList<String>();
		for(HashMap.Entry<String,String> entry : data.entrySet()){
			if(entry.getKey().contains("item_") && entry.getKey().compareTo("item_count")!=0){
				aux.add(entry.getValue());		
			}
		}			
		res="Historic of searches:\n";
		for(int i=aux.size()-1;i>=0;i--){
			res+=aux.get(i)+"\n";
		}
		this.historic=aux;
		checkRequestID(res, data);
	}			

	/**
	 * generates request ID
	 * @return ID
	 */
	public static String generateID(){
		String alpha = "0123456789";
		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) { 
            int index = (int)(alpha.length()*Math.random()); 
            sb.append(alpha.charAt(index)); 
        }
		return sb.toString();
	}
	
	public void checkRequestID(String res, HashMap<String, String> data){
		for(ClientRequest r: requests){
			if(r.request_id.compareTo(data.get("request_ID"))==0){
				if(r.client!=null) {
					try{
						r.client.print_on_client(res);
					} catch(RemoteException e){
						System.out.println("Could not send message to client");
					}
				}
			}
		}		
	}
	
	//////////////////////////////////////////////////////////// ADMIN page real time////////////////////////////////////////
	/**
	 * get multicast info (port, IP, ID) and store it in Multicast servers list
	 * @param multicastID
	 */
	public void getMulticastInfo( String multicastID){
		int aux=0;
		if(!multicastData.isEmpty()) {
			for(MulticastData mul: multicastData) {
				if(mul.id.compareTo(multicastID)!=0) {
					aux=1;				
				}
			}
			if(aux==1) {
				MulticastData m=new MulticastData(multicastID, MULTICAST_ADDRESS, PORT_MULT);
				int i=0;
				for(ClientRequest c: requests) {
					if(admins.contains(c.username)) {
						try{
							c.client.print_on_client("MULTICAST SERVERS INFO");
							c.client.print_on_client("ID: "+m.id+", IP: "+MULTICAST_ADDRESS+", Port: "+PORT_MULT+"\n");
							multicastData.add(m);
							i++;
							if(i==1)
								break;
						} catch(RemoteException e){
							System.out.println("Could not send message to client");
						}							
					}
				}			
			}			
		}
		else {
			MulticastData m=new MulticastData(multicastID, MULTICAST_ADDRESS, PORT_MULT);
			for(ClientRequest c: requests) {
				if(admins.contains(c.username)) {
					try{
						c.client.print_on_client("MULTICAST SERVERS INFO");
						c.client.print_on_client("ID: "+m.id+", IP: "+MULTICAST_ADDRESS+", Port: "+PORT_MULT+"\n");
						multicastData.add(m);
						break;
					} catch(RemoteException e){
						System.out.println("Could not send message to client");
					}						
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * send Multicast info to ADMIN 
	 * @param data: message data from Multicast server
	 */
	public void receiveMulticastData(HashMap<String, String> data){
		for(ClientRequest r: requests){
			if(r.request_id.compareTo(data.get("request_ID"))==0){
				try{
					r.client.print_on_client("MULTICAST SERVERS INFO");
					if(!multicastData.isEmpty()) {
						for(MulticastData m: multicastData)
							r.client.print_on_client("ID: "+m.id+", IP: "+MULTICAST_ADDRESS+", Port: "+PORT_MULT+"\n");
					}
				} catch(RemoteException e){
					System.out.println("Could not send message to client: "+e);
				}
			}
		}
	}
	
	/**
	 * if the time out is reached (30 seconds), a message is sent to the client, saying
	 * that Multicast server is off
	 * @param client_id
	 */
	public void multicastDownMessage(String username){
		for(ClientRequest r: requests){
			if(r.username.compareTo(username)==0){
				if(r.client!=null) {
					try{
						r.client.print_on_client("Multicast server is off. Try again later!");
					} catch(RemoteException e){
						System.out.println("Could not send message to client: "+e);
					} 
				}
			}
		}
	}

	/**
	 * opens a new socket to ask for Multicast server ID
	 * after receiving the ID, the socket is closed
	 * @param client_id
	 * @return Multicast ID
	 */
	public String askMulticastID(String username){
		MulticastSocket socket1 = null;
		String multId="";
		try{
			socket1 = new MulticastSocket(PORT_MULT); 
			InetAddress group1 = InetAddress.getByName(MULTICAST_ADDRESS);
			socket1.joinGroup(group1);
			
			//ask for Multicast server ID
			byte[] buffer = "Send your ID, please".getBytes();	            
			DatagramPacket send_packet = new DatagramPacket(buffer, buffer.length, group1, PORT_MULT);
			socket1.send(send_packet);	

			//receive id from Multicast server
			while(true){
				buffer = new byte[8*1024];	
				DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);
				
				//wait for multicast response
				socket1.setSoTimeout(30000);
				
				socket1.receive(receive_packet);      
				multId = new String(receive_packet.getData(), 0, receive_packet.getLength());				
				if(multId.charAt(0)=='#'){
					break;
				}
			}			
			socket1.close();
		} catch (IOException re) {		
			System.out.println("here1");
			System.out.println("Multicast server is off!");
			System.out.println(re);
			socket1.close();
			multicastDownMessage(username);	
		}
		return multId;
	}
	
	/**
	 * after receiving a command and the client data, it constructs the message to send to the MulticastServer
	 * @param command typed by client
	 * @param message1 - can be username, or another info depending on chosen command 
	 * @param message2 - can be password, or another info depending on chosen command 
	 * @param c: client interface
	 * @param client_id
	 */
	public void sendToMulticast(String command, String message1, String message2, RMI_interface_client_methods c) {				
		MulticastSocket socket = null;		
		String messageReceived="";	
		String multicastID="";
		String clientRequest="";
		ClientRequest n;
		byte[] buffer;
		
		try{
			socket = new MulticastSocket(PORT_MULT); 
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
			while(true){
				if(command.compareTo("offline")==0 || command.compareTo("registry")==0 || command.compareTo("login")==0 || command.compareTo("logout")==0 || command.compareTo("registers_list")==0 || command.compareTo("admin_privileges")==0 || command.compareTo("index_url")==0 || command.compareTo("search")==0 || command.compareTo("my_searches")==0){					
					//generate requestID
					String request_id = generateID();	
					
					//put new request in requests list
					n=new ClientRequest(request_id, message1, c);
					requests.add(n);
					
					//get multicast server ID
					multicastID=askMulticastID(message1);
					
					//get multicast info to send to admin
					getMulticastInfo(multicastID);						
					
					if(command.compareTo("offline")==0){
						clientRequest="type | offline ; username | " + message1 + " ; request_id | " + request_id + " ; multicast_ID | "+ multicastID;
					}
					if(command.compareTo("registry")==0){
						clientRequest="type | registry ; username | " + message1 + " ; password | " + message2 + " ; request_id | " + request_id + " ; multicast_ID | "+ multicastID;										
					}
					else if(command.compareTo("login")==0){
						clientRequest="type | login ; username | " + message1 + " ; password | " + message2 + " ; request_id | " + request_id + " ; multicast_ID | " + multicastID;
					}
					else if(command.compareTo("logout")==0){						
						clientRequest="type | logout ; username | " + message1 + " ; password | " + message2 + " ; request_id | " + request_id + " ; multicast_ID | " + multicastID;
					}
					else if(command.compareTo("registers_list")==0){
						clientRequest="type | registers_list ; username | " + message1 + " ; request_id | " + request_id + " ; multicast_ID | "+ multicastID;
					}
					else if(command.compareTo("admin_privileges")==0){
						clientRequest="type | admin_privileges ; usernameAdmin | " + message1 + " ; usernameDest | " + message2 + " ; request_id | " + request_id + " ; multicast_ID | "+ multicastID;
					}
					else if(command.compareTo("index_url")==0){
						clientRequest="type | index_url ; username | " + message1 + " ; URL | " + message2 + " ; request_id | " + request_id + " ; multicast_ID | " + multicastID; 
					}
					else if(command.compareTo("search")==0){
						clientRequest="type | search ; text | " + message1 + " ; username | " + message2 + " ; request_id | " + request_id + " ; multicast_ID | " + multicastID; 
					}					
					else if(command.compareTo("my_searches")==0){
						clientRequest="type | my_searches ; username | " + message1 + " ; request_id | " + request_id + " ; multicast_ID | " + multicastID; 
					}
					
					//send clientRequest message to Multicast server
					buffer = clientRequest.getBytes();	            
					DatagramPacket send_packet1 = new DatagramPacket(buffer, buffer.length, group, PORT_MULT);
					socket.send(send_packet1);	

					//receive Multicast response about that client request
					while(true){												
						buffer = new byte[10*1024];	
						DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);
						
						//time limit to wait for Multicast response
						//socket.setSoTimeout(30000);
						
						socket.receive(receive_packet);      
						messageReceived = new String(receive_packet.getData(), 0, receive_packet.getLength());	

						//login, logout and register
						if(messageReceived.contains("status")){	
							handleMulticastResponse(messageReceived, c, message1);
							break;
						}
						//ADMIN page
						else if(messageReceived.contains("requests_list_allowed") || messageReceived.contains("registersList_Not_Admin!") || messageReceived.contains("registersList_Not_logged!")){							
							handleMulticastResponse(messageReceived, c, message1);
							break;
						}
						//ADMIN privileges
						else if(messageReceived.contains("_is_ADMIN!") || messageReceived.contains("adminPrivileges_Unexistent_user!") || messageReceived.contains("adminPrivileges_Must_be_ADMIN!")){
							handleMulticastResponse(messageReceived, c, message1);
							break;
						}
						//URL index
						else if(messageReceived.contains("URLIndex_indexed!") || messageReceived.contains("URLIndex_Must_be_ADMIN!") || messageReceived.contains("URLIndex_Must_be_logged!")){
							handleMulticastResponse(messageReceived, c, message1);
							break;
						}
						//new search
						else if(messageReceived.contains("search_results ; item_count") || messageReceived.contains("search_results ; msg")){
							handleMulticastResponse(messageReceived, c, message2);
							break;
						}
						//list of client searches
						else if(messageReceived.contains("yourSearches ; item_count") || messageReceived.contains("yourSearches_Not_found!") || messageReceived.contains("yourSearches_Must_be_registered_logged!")){
							handleMulticastResponse(messageReceived, c, message1);
							break;
						}
					}
				}
				break;				
			} 
		} catch (IOException re) {
			//System.out.println("here2");	
			//System.out.println(re);	
			//if Multicast server fails, the client is notified after 30s  
			multicastDownMessage(message1);	
		} catch (Exception e) {
			//System.out.println("here3");
			//System.out.println(e);	
		} 
	}
	
	public static void main(String args[]) {	
		boolean serverOn = true;		
		while(true){
			//if first server is on
			if(serverOn){				
				try {
					RMIServer h = new RMIServer();
					Registry r = LocateRegistry.createRegistry(PORT_RMI);
					r.rebind(args[0], h);
					System.out.println("----------------RMI Server ready----------------");				
					while(true){}
					
				//the first server is connected, so the second server becomes secondary
				} catch (IOException re) {
					System.out.println("Turning Server Secundary!");
	                serverOn = false;
				} catch (Exception e) {
					System.out.println("Exception!");
				} 
			}
			//sleeps until first server fail, then the second server becomes the first one
			if(!serverOn){
				try{
					//checks from 5 in 5 seconds if first server fails
					while (true) {
		                System.out.println("Sleeping for some time!");		                
		                Thread.sleep(3000);
		                serverOn = true;
		                break;
		            }
				} catch (InterruptedException e){
					System.out.println("Exception in main!");
				}
			}
		}
	}
}

