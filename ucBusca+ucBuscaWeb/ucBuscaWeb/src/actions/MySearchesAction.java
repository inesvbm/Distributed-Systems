package actions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.MySearchesBean;

public class MySearchesAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username=null;
	private ArrayList<String> mySearches = null;
	
	@Override
	public String execute() throws RemoteException{
		String user=this.session.get("username").toString();
		this.getMySearchesBean().setUsername(user);
		
		if(getMySearchesBean().mySearchesBean()) {
			session.put("historic", true);
			this.mySearches=this.getMySearchesBean().getMySearches();
		}
		else {
			session.put("historic", false);
		}
		return SUCCESS;
	}
	
	/**
	 * getter/setter
	 * @return username
	 */
	public String getUsername(){
		return username;  
	}
	
	public void setUsername(String username){
		this.username = username;  
	}

	/**
	 * getter/setter
	 * @return client historic of searches
	 */
	public ArrayList<String> getMySearches(){
		return mySearches;  
	}

	public void setMySearches(ArrayList<String> mySearches) {
		this.mySearches = mySearches;
	}

	public void setResult(ArrayList<String> mySearches){
		this.mySearches = mySearches;  
	}
	
	/**
	 * 
	 * @return MySearchesBean
	 * @throws RemoteException
	 */
	public MySearchesBean getMySearchesBean() throws RemoteException{
		if(!session.containsKey("MySearchesBean"))
			this.setMySearchesBean(new MySearchesBean());
		return (MySearchesBean) session.get("MySearchesBean");
	}

	public void setMySearchesBean(MySearchesBean MySearchesBean){
		this.session.put("MySearchesBean", MySearchesBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
