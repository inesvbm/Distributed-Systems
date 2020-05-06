package ucBusca;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
/**
 * 
 * @author inesv
 *
 */
public class RequestHandler extends Thread{
	public MulticastServer server;
	//message received from RMI server
	public String message;
	//message that will be sent to RMI server
	public String response="";

	/**
	 * to synchronized handling of clients requests
	 * to handle the request
	 * @param server: multicast server
	 * @param message: string with client request  
	 */
	public RequestHandler(MulticastServer server, String message){		
		this.server=server;
		this.message=message;
		this.start();
	}
	
	/**
	 * checks if the client is already in registers list
	 * @param client_id: ID from the client who made the request
	 * @return boolean: says if he is already registered
	 */
	public boolean clientIsRegistered(String username){
		int i=0;
		boolean aux=false;
		while(i<MulticastServer.registeredClients.size()){			
			if(MulticastServer.registeredClients.get(i).username.compareTo(username)==0){
				aux=true;
				break;
			}
			i++;
		}		
		return aux;
	}
	
	/**
	 * checks if the client is already in logged clients list
	 * @param client_id: ID from the client who made the request
	 * @return boolean: says if he is already logged
	 */
	public boolean clientIsLogged(String username){
		int i=0;
		boolean aux=false;
		while(i<MulticastServer.loggedClients.size()){
			if(MulticastServer.loggedClients.get(i).username.compareTo(username)==0){
				aux=true;
				break;
			}
			i++;
		}		
		return aux;
	}
	
	/**
	 * checks if the client is already in registers list and if he is also ADMIN
	 * @param client_id: ID from the client who made the request
	 * @return boolean: says if he is registered and ADMIN
	 */
	public boolean clientIsRegistered_admin(String username){
		int i=0;
		boolean aux=false;
		while(i<MulticastServer.registeredClients.size()){
			if(MulticastServer.registeredClients.get(i).username.compareTo(username)==0 && MulticastServer.registeredClients.get(i).admin.compareTo("yes")==0){
				aux=true;
				break;
			}
			i++;
		}
		return aux;
	}
	
	/**
	 * when client goes off, his username is removed from logged clients list
	 * @param data: from message received from RMI server
	 */
	public void offlineHandler(HashMap<String, String> data){
		ArrayList<RegisteredClients> aux=new ArrayList<RegisteredClients>();
		for(RegisteredClients l: MulticastServer.loggedClients) {
			if(l.username.compareTo(data.get("username"))!=0) {
				aux.add(l);
			}				
		}
		MulticastServer.loggedClients=aux;
	}
	
	/**
	 * handles registry messages from clients
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String registerHandler(HashMap<String, String> data){
		String username=data.get("username");
		String password=data.get("password");
		String res="";

		//check if client is already registered
		boolean client_is_registered=clientIsRegistered(username);
		
		if(client_is_registered==false){
			RegisteredClients registered=new RegisteredClients(username, password, "no");
			//if is the first register, become ADMIN
			if(MulticastServer.registeredClients.size()==0) {
				registered.admin="yes";
			}
			else {
				registered.admin="no";	
			}
			MulticastServer.registeredClients.add(registered);
			res="request_ID | " + data.get("request_id") + " ; type | status ; username | " + username + " ; registered | yes ; msg | Registered!";
		}
		else{
			res="request_ID | " + data.get("request_id") + " ; type | status ; username | " + username + " ; registered | yes ; msg | Already_registered!";
		}
		return res;
	}
	
	/**
	 * handles login messages from clients
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String loginHandler(HashMap<String, String> data){
		String username=data.get("username");
		String password=data.get("password");
		RegisteredClients log;
		String res="";	
		boolean aux=false;
		String admin="";
		//check if client is registered and already logged
		boolean client_is_registered=clientIsRegistered(username);		
		boolean client_is_logged=clientIsLogged(username);
		
		if(client_is_registered==true && client_is_logged==false){
			for(RegisteredClients r: MulticastServer.registeredClients){
				if(username.compareTo(r.username)==0 && password.compareTo(r.password)==0) {
					aux=true;
					admin=r.admin;
				}				
			}
			if(aux==true){				
				log=new RegisteredClients(username, password, "");
				MulticastServer.loggedClients.add(log);
				res="request_ID | " + data.get("request_id") + " ; username | " + username + " ; admin | " + admin + " ; type | status ; logged | on ; msg | Logged!";
			}
			else {
				res="request_ID | " + data.get("request_id") + " ; type | status ; logged | off ; msg | Wrong_username_password!";
			}
		}
		else if(client_is_registered==true && client_is_logged==true){
			res="request_ID | " + data.get("request_id") + " ; type | status ; logged | on ; msg | Already_logged!";
		}
		else{
			res="request_ID | " + data.get("request_id") + " ; type | status ; logged | off ; msg | Wrong_username_password!";
		}
		return res;
	}
	
	/**
	 * handles logout messages from clients
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String logoutHandler(HashMap<String, String> data){
		String username=data.get("username");
		String password=data.get("password");
		String res="";	
		ArrayList<RegisteredClients> newLog=new ArrayList<RegisteredClients>();

		for(RegisteredClients r: MulticastServer.loggedClients){
			if(username.compareTo(r.username)!=0 && password.compareTo(r.password)!=0) {
				newLog.add(r);
			}
		}		
		MulticastServer.loggedClients=newLog;
		res="request_ID | " + data.get("request_id") + " ; type | status ; msg | Logout!";
		return res;
	}
	
	/**
	 * updates ADMIN page: gets registers list and common searches list (Multicast info is handled in the RMI server) 
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String registersListHandler(HashMap<String, String> data){				
		String username=data.get("username");
		String res="";		
		//checks if client is registered, ADMIN and logged
		boolean client_is_admin=clientIsRegistered_admin(username);		
		boolean client_is_logged=clientIsLogged(username);
		
		if(client_is_admin==true && client_is_logged==true){	
			res+="request_ID | " + data.get("request_id") + " ; type | requests_list_allowed ; username | " + username;
		}
		else if(client_is_logged==false) {
			res="request_ID | " + data.get("request_id") + " ; type | requests_list_permission ; msg | registersList_Not_logged!";
		}
		else if(client_is_admin==false) {
			res="request_ID | " + data.get("request_id") + " ; type | requests_list_permission ; msg | registersList_Not_Admin!";
		}
		return res;
	}
	
	/**
	 * 
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String adminPrivilegesHandler(HashMap<String, String> data){
		String res="";
		String username_dest=data.get("usernameDest");
		String username_admin=data.get("usernameAdmin");
		//checks if client is registered, ADMIN and logged
		boolean client_is_admin=clientIsRegistered_admin(username_admin);	
		boolean client_is_logged=clientIsLogged(username_admin);		
		
		if(client_is_admin==true && client_is_logged==true){
			for(RegisteredClients r: MulticastServer.registeredClients){
				if(r.username.compareTo(username_dest)==0){
					r.admin="yes";
					return res="request_ID | " + data.get("request_id") + " ; type | admin_privileges ; usernameAdmin | "+ username_admin +" ; usernameDest | " + username_dest + " ; msg | user_" + username_dest +"_is_ADMIN!";
				}
				else{
					res="request_ID | " + data.get("request_id") + " ; type | admin_privileges ; msg | adminPrivileges_Unexistent_user!";
				}
			}
		}	
		else{
			res="request_ID | " + data.get("request_id") + " ; type | admin_privileges ; msg | adminPrivileges_Must_be_ADMIN!";
		}		
		return res;
	}
	
	/**
	 * calls InternetAccess class to index a new URL
	 * gets all words it contains, associated links, title and description 
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String indexURLHandler(HashMap<String, String> data){
		String username=data.get("username");
		String res="";
		String url=data.get("URL");	
		//checks if client is registered, ADMIN and logged
		boolean client_is_admin=clientIsRegistered_admin(username);	
		boolean client_is_logged=clientIsLogged(username);
		
		if(client_is_admin==true && client_is_logged==true){
			InternetAccess ia=new InternetAccess(url);			
			ia.readClientInput(url);						
			for(String word: InternetAccess.countMap.keySet()) {
				SearchData search=new SearchData(InternetAccess.title, InternetAccess.text1, url, InternetAccess.linksAssociated);
				if(!MulticastServer.index.containsKey(word)){
					ArrayList<SearchData> s=new ArrayList<SearchData>();
					s.add(search);
					MulticastServer.index.put(word, s);					
				}
				else{
					int aux=0;
					ArrayList<SearchData> s1=MulticastServer.index.get(word);
					for(SearchData d: s1){
						if(d.url.compareTo(url)!=0)
							aux=1;
					}
					if(aux==1)
						s1.add(search);
				}
			}
			
			//add associated links from main URL to URL queue
			MulticastServer.url_queue.clear();
			for(String link: InternetAccess.linksAssociated){
				if(!MulticastServer.url_queue.contains(link))
					MulticastServer.url_queue.add(link);
			}	
			
			new Indexing();
			res="request_ID | " + data.get("request_id") + " ; type | index_url ; msg | URLIndex_indexed!";

			//create URLs index
			ArrayList<SearchNumber> found_all_urls=new ArrayList<SearchNumber>();
			for(ArrayList<SearchData> a: MulticastServer.index.values()){
				for(SearchData p: a){
					SearchNumber se=new SearchNumber(p.url, p.linksAssoc.size());
					found_all_urls.add(se);					
				}
			}
			Collections.sort(found_all_urls, new Comparator<SearchNumber>() {
			    @Override
			    public int compare(SearchNumber o1, SearchNumber o2) {	    	
			        return o2.count-o1.count;
			    }
			});
			
			/*res+=" ; Important_Urls | ";
			int i=0;
			while(i<10) {
				res+=found_all_urls.get(i).text+"----";
				i++;
			}*/
		}
		else if(client_is_admin==false){
			res="request_ID | " + data.get("request_id") + " ; type | index_url ; msg | URLIndex_Must_be_ADMIN!";
		}
		else if(client_is_logged==false){
			res="request_ID | " + data.get("request_id") + " ; type | index_url ; msg | URLIndex_Must_be_logged!";
		}		
		return res;
	}
	
	/**
	 * adds the new search to client searches HashMap
	 * @param text: that client sent as a new search
	 * @param client_id
	 */
	public void clientSearches(String text, String username){
		//displays index
		for(String word: MulticastServer.index.keySet()){
			ArrayList <SearchData> aux = MulticastServer.index.get(word);
			for(SearchData d : aux){
				word+="----"+d.url;
			}
			System.out.println(word);
		}
		
		//if it is first client search, add it to the list
		if(!MulticastServer.client_searches.containsKey(username)){
			HashSet<String> searches=new HashSet<String>();
			searches.add(text);
			MulticastServer.client_searches.put(username, searches);
		}
		//if not, gets client ID and store new search to his list of searches
		else{
			HashSet<String> searches=MulticastServer.client_searches.get(username);
			searches.add(text);
		}
	}

	/**
	 * when a client makes a new search, it tries to find it in the index list (see comments in method, bellow)
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String searchHandler(HashMap<String, String> data){
		String res="";
		String username="";
		if(data.containsKey("username"))
			username=data.get("username");
		String text=data.get("text");		
		String[] words=text.split(" ");		

		//add new search to client searches list
		if(username.compareTo("none")!=0)
			clientSearches(text, username);
				
		HashMap<String, ArrayList<SearchData>> found_words=new HashMap<String, ArrayList<SearchData>>();
		//search words in the index list
		for(String index_word: MulticastServer.index.keySet()){
			ArrayList <SearchData> aux = MulticastServer.index.get(index_word);
			for(int i=0;i<words.length;i++){
				//if found, put them in the new HashMap found_words
				if(words[i].compareTo(index_word)==0){
					found_words.put(index_word, aux);
				}		
			}
		}

		//if words were not found, return
		if(found_words.isEmpty()) {
			return res="request_ID | " + data.get("request_id") + " ; type | search_results ; msg | searchText_Not_found!";
		}
		
		//create a HashMap with URLs that match found words; URL as keys and a PageData (title, description, URL list) as value
		//this will make easy to add final results to the results list bellow
		HashMap<String, PageData> found_urls=new HashMap<String, PageData>();
		for(ArrayList<SearchData> a: found_words.values()){
			for(SearchData p: a){				
				if(!found_urls.containsKey(p.url)){
					HashSet<String> list=p.linksAssoc;					
					PageData pd=new PageData(p.title, p.description, p.url, list);
					found_urls.put(p.url, pd);
				}
			}
		}

		//final results
		List<PageData> results=new ArrayList<PageData>();
		//add details from URLs HashMap to the final results
		for(PageData u: found_urls.values()){
			results.add(u);
		}

		////////////////////////////////////////// get connections to main URL //////////////////////////////////////////
		
		//create URLs index
		HashMap<String, PageData> found_all_urls=new HashMap<String, PageData>();
		for(ArrayList<SearchData> a: MulticastServer.index.values()){
			for(SearchData p: a){
				if(!found_all_urls.containsKey(p.url)){
					HashSet<String> list=p.linksAssoc;					
					PageData pd=new PageData(p.title, p.description, p.url, list);
					found_all_urls.put(p.url, pd);
				}
			}
		}	

		//get connections
		for(PageData d: results){
			for(String s: found_all_urls.keySet()) {
				if(s.contains(d.name) && s.compareTo(d.name)!=0){
					d.connections_to.add(s);
					d.count_links++;
				}
			}
		}

		res="request_ID | " + data.get("request_id") + " ; type | search_results ; item_count | " + results.size()+" ; ";
		for(int i=0;i<results.size();i++){
			res+="item_" + i + "_details | " + results.get(i).title + "----" + results.get(i).description+ "----" + results.get(i).name + "----" + results.get(i).count_links + "----";	
			
			//////////////////////////////////////////////////////////// CONECTIONS ONLY APPEAR IF CLIENT IS LOGGED!!!!! //////////////////////////////////////////////////////
			if(!username.equals("none")) {
				if(results.get(i).count_links!=0){
					for(String s: results.get(i).connections_to){
						res+=s + "----";
					}					
				}
			}
			res=res.substring(0, res.length()-4);		
			res+=" ; ";
		}

		//update common searches list	
		if(!MulticastServer.common_searches.contains(text))
			MulticastServer.common_searches.add(text);
		
		res+="Common_searches | ";
		int i=MulticastServer.common_searches.size()-1;
		while(i>=0) {
			res+=MulticastServer.common_searches.get(i)+"----";
			i--;
		}
		
		return res;	
	}
	
	/**
	 * gets a client list of searches 
	 * @param data: from message received from RMI server
	 * @return response to send to the RMI server
	 */
	public String mySearchesHandler(HashMap<String, String> data){
		String username=data.get("username");		
		String res="";
		//ckecks if client is registered, ADMIN and logged
		boolean client_is_registered=clientIsRegistered(username);		
		boolean client_is_logged=clientIsLogged(username);
		int i=0;
		if(client_is_registered==true && client_is_logged==true){			
			if(MulticastServer.client_searches.containsKey(username)){								
				res="request_ID | " + data.get("request_id") + " ; type | yourSearches ; item_count | " + MulticastServer.client_searches.get(username).size() + " ; ";
				for(String text: MulticastServer.client_searches.get(username)){
					res+="item_" + i + "_description | " + text + " ; ";
					i++;
				}
			}			
			else{
				res="request_ID | " + data.get("request_id") + " ; type | yourSearches ; msg | yourSearches_Not_found!";
			}
		}
		else{
			res="request_ID | " + data.get("request_id") + " ; type | yourSearches ; msg | yourSearches_Must_be_registered_logged!";
		}	
		return res;
	}

	/**
	 * splits message to send to the corresponding handle method
	 * @param message: from client, received from RMI client
	 * @return response to the message, to send to RMI server
	 */
	public String splitMessage(String message){
		HashMap<String, String> data=new HashMap<String, String>();
		message=message.replaceAll(" \\| ", "|");
		message=message.replaceAll(" ; ", ";");
		String[] pairsKV=message.split(";");	
		String[] keyValue;
		String res="";
		for(int i=0;i<pairsKV.length;i++){	
			keyValue=pairsKV[i].split("\\|");
			data.put(keyValue[0], keyValue[1]);
		}
		if(data.containsValue("offline")){
			offlineHandler(data);
		}
		if(data.containsValue("registry")){
			res=registerHandler(data);
		}
		else if(data.containsValue("login")){			
			res=loginHandler(data);
		}
		else if(data.containsValue("logout")){	
			res=logoutHandler(data);
		}
		else if(data.containsValue("registers_list")){			
			res=registersListHandler(data);
		}
		else if(data.containsValue("admin_privileges")){			
			res=adminPrivilegesHandler(data);
		}
		else if(data.containsValue("index_url")){	
			res=indexURLHandler(data);
		}
		else if(data.containsValue("search")){	
			res=searchHandler(data);
		}	
		else if(data.containsValue("my_searches")){	
			res=mySearchesHandler(data);
		}
		return res;
	}
	
	public void run(){
		MulticastSocket socket=null;		
		try{			
			socket = new MulticastSocket();  
            InetAddress group = InetAddress.getByName(MulticastServer.MULTICAST_ADDRESS);
            socket.joinGroup(group);
            //if the message is to ask for Multicast ID, send the ID
			if(message.compareTo("Send your ID, please")==0){            	
            	byte[] buffer1 = server.id.getBytes();	            
                DatagramPacket send_packet = new DatagramPacket(buffer1, buffer1.length, group, MulticastServer.PORT_MULT);
                socket.send(send_packet);	                
            }
			//if it is another message
			else if(message.contains("offline") || message.contains("registry") || message.contains("login") || message.contains("logout") || message.contains("registers_list") || message.contains("admin_privileges") || message.contains("index_url") || message.contains("search") || message.contains("my_searches")){								
				//checks if the message has this Multicast server ID. If yes, this server will handle it. If not, it will ignore it
				if(message.contains(server.id)){
					System.out.println(server.id+": IT'S FOR ME!");
					System.out.println(message);
					System.out.println(server.id+"\n");
        			response=splitMessage(message);
            		byte[] buffer1 = response.getBytes();
	                DatagramPacket send_packet = new DatagramPacket(buffer1, buffer1.length, group, MulticastServer.PORT_MULT);
	                socket.send(send_packet);	
        		}
            }
		} catch (IOException e) {
            System.out.println("Could not handle request! "+e);
        } 
	}
}

class Indexing extends Thread {	
	Indexing(){	
		this.start();
	}
	public void run() {	
		int i=0;
		while(i<20){
			String url=MulticastServer.url_queue.get(i);
			InternetAccess ia=new InternetAccess(url);			
			ia.readClientInput(url);						
			for(String word: InternetAccess.countMap.keySet()) {
				SearchData search=new SearchData(InternetAccess.title, InternetAccess.text1, url, InternetAccess.linksAssociated);
				if(!MulticastServer.index.containsKey(word)){
					ArrayList<SearchData> s=new ArrayList<SearchData>();
					s.add(search);
					MulticastServer.index.put(word, s);					
				}
				else{
					int aux=0;
					ArrayList<SearchData> s1=MulticastServer.index.get(word);
					for(SearchData d: s1){
						if(d.url.compareTo(url)!=0)
							aux=1;
					}
					if(aux==1)
						s1.add(search);
				}						
			}
			//add associated links to URL queue
			for(String link1: InternetAccess.linksAssociated)
				MulticastServer.url_queue.add(link1);
			i++;
    		if(i==19) {
    			break; 
    		}
		}
	}				
}



