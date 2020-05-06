package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.net.URLConnection;

import ucBusca.RMI_interface_server_methods;

public class SearchBean extends UnicastRemoteObject {
	private static final long serialVersionUID = 1L;
	private static int PORT_RMI=5614;	
	private RMI_interface_server_methods server;
	private String search;
	private String username;
	private ArrayList<ArrayList<String>> result = null;
	public void setResult(ArrayList<ArrayList<String>> result) {
		this.result = result;
	}

	public static final String API_KEY = "trnsl.1.1.20191215T184915Z.a43d3770202b81d7.7ffc5ad1ac6977ff1bf650e06659390f327b8ea9";

	public SearchBean() throws RemoteException{
		try {
			server = (RMI_interface_server_methods) LocateRegistry.getRegistry(PORT_RMI).lookup("localhost");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace();
		}	
	}	
	
	/**
	 * 
	 * @return boolean: found search results
	 * @throws RemoteException
	 */
	public boolean searchBean() throws RemoteException {
		if(server.newSearchWeb(this.username, this.search).size()!=0) {
			return true;
		}
		return false;		
	}
	
	//////////////////////////////////////////////////////////////////////////////// YANDEX /////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param text: page title
	 * @return page language
	 * @throws IOException
	 */
	public static String checkLanguage(String text) throws IOException {
		String yandexDetect = "https://translate.yandex.net/api/v1.5/tr.json/detect";
		String completeURL = yandexDetect+"?key=" + API_KEY + "&text="+text;
		if(completeURL.contains(" "))
			completeURL = completeURL.replace(" ", "%20");	
		URL url = new URL(completeURL);
		URLConnection urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent", "Mozilla");
		
		InputStream inStream = urlConn.getInputStream();
		
		String recieved = new BufferedReader(new InputStreamReader(inStream)).readLine();
		
		inStream.close();
		return recieved.substring(recieved.indexOf("lang")+7, recieved.length()-2);
	}
	
	public static String translate(String text, String sourceLang) throws IOException {
		String completeURL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + API_KEY + "&text=" + text + "&lang=" + sourceLang + "-pt";
		if(completeURL.contains(" "))
			completeURL = completeURL.replace(" ", "%20");
		if(completeURL.contains(" #"))
			completeURL = completeURL.replace(" #", "%20");

		URL url = new URL(completeURL);
		URLConnection urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent", "Mozilla");
		urlConn.setRequestProperty("Accept-Charset", "UTF-8");
		
		InputStream inStream = urlConn.getInputStream();
		InputStreamReader in = new InputStreamReader(inStream, "UTF-8");
		String translation = new BufferedReader(in).readLine();
		inStream.close();		
		
		translation=translation.replace("{", "");
		translation=translation.replace("}", "");
		translation=translation.replace("[", "");
		translation=translation.replace("]", "");		
		translation=translation.replace("\"", "");
		translation=translation.replace("#", "");
		translation=translation.replace("?", "/?");
		
		return translation;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * getter/setter
	 * @return search
	 */
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
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
	 * @return search results
	 * @throws RemoteException
	 */
	public ArrayList<ArrayList<String>> getResult() throws RemoteException{
		return server.newSearchWeb(this.username, this.search);
	}	

}
