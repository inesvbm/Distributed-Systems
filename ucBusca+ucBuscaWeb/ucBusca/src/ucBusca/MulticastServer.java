package ucBusca;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/**
 * 
 * @author inesv
 *
 */
public class MulticastServer extends Thread{	
	public static String MULTICAST_ADDRESS = "224.3.2.1";
	public static int PORT_MULT = 7813;
	//list of registered clients
	public static ArrayList<RegisteredClients> registeredClients=new ArrayList<RegisteredClients>();
	//list of logged clients (client ID, username, password, ADMIN)
	public static ArrayList<RegisteredClients> loggedClients=new ArrayList<RegisteredClients>();
	//word index list (word, list with URL and other details)
	public static HashMap<String, ArrayList<SearchData>> index=new HashMap<String, ArrayList<SearchData>>();
	//clients_searches list (client ID, list of searches)
	public static HashMap<String, HashSet<String>> client_searches=new HashMap<String, HashSet<String>>();	
	//list of most common searches
	public static ArrayList<String> common_searches=new ArrayList<String>();
	//URL queue
	public static ArrayList<String> url_queue=new ArrayList<String>();
	//ID from Multicast server
	public String id="";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//read from file
		try{     
	        FileInputStream fis2 = new FileInputStream("index.ser");
	        ObjectInputStream ois2 = new ObjectInputStream(fis2);
	        index=(HashMap<String, ArrayList<SearchData>>)ois2.readObject();
	        ois2.close();
	        
		} catch(IOException e){
			System.out.println("Files are empty!");
		} catch(Exception e){
			System.out.println("Files are empty!");
		}
		
		MulticastServer server = new MulticastServer();
		//generate multicast ID
		server.id=generateMulticastServerID();		
		server.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run(){
            	try{    	
	    			FileOutputStream  fos2 = new FileOutputStream("index.ser");
	    			ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
	    			oos2.writeObject(index);
	    			oos2.close();
	    	        
	                System.out.println("Writing data to files");
            	} catch (IOException e){
            		System.out.println("Could not save data!");
            	} catch (Exception e){
            		System.out.println("Could not save data!");
            	}
            }
        });
	}
	
	public static String generateMulticastServerID(){
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) { 
            int index = (int)(alpha.length()*Math.random()); 
            sb.append(alpha.charAt(index)); 
        }
		return "#"+sb.toString();
	}

	public void run() {		
        MulticastSocket socket = null;  
        try {
            socket = new MulticastSocket(PORT_MULT);  
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            System.out.println("----------------Multicast Server ready----------------");
            
            while(true) { 
            	//receive message from RMI server                       	
            	byte[] buffer = new byte[10*1024];
                DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(receive_packet);
                String message = new String(receive_packet.getData(), 0, receive_packet.getLength()); 
                new RequestHandler(this, message);                 
            }                       
        } catch (IOException e) {
        	System.out.println("Could not handle request!");
        }
    }
}



