package actions;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.URLBean;

public class URLAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String url = null;

	@Override
	public String execute() throws RemoteException{
		String user="";
		if(this.url != null && !url.equals("")) {
			this.getURLBean().setUrl(this.url);	
			
			//find admin username in session
			if(session.containsKey("username")) {
				user=this.session.get("username").toString();
				this.getURLBean().setUsername(user);
			}
			else {
				this.getURLBean().setUsername("none");		
			}
						
			if(getURLBean().urlBean()) {
				session.put("URLindexed", true); 
				return SUCCESS;	
			}
			return LOGIN;
		}
		else
			return LOGIN;
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
	 * @param url
	 */
	public void setUrl(String url){
		this.url = url;  
	}
	
	/**
	 * getter/setter
	 * @return URLBean
	 * @throws RemoteException
	 */
	public URLBean getURLBean() throws RemoteException{
		if(!session.containsKey("URLBean"))
			this.setURLBean(new URLBean());
		return (URLBean) session.get("URLBean");
	}

	public void setURLBean(URLBean URLBean){
		this.session.put("URLBean", URLBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
