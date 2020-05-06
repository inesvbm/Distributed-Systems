package actions;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.AdminPrivBean;

public class AdminPrivAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String clientUsername = null;

	/**
	 * verify if client became admin
	 */
	@Override
	public String execute() throws RemoteException{	
		if(this.clientUsername != null && !clientUsername.equals("")) {
			
			//find admin username in session
			String user=this.session.get("username").toString();
			this.getAdminPrivBean().setUsername(user);		
			
			//set client destination username
			this.getAdminPrivBean().setClientUsername(this.clientUsername);	
			
			//if new client became admin
			if(this.getAdminPrivBean().adminPrivileges()) {
				System.out.println("here");
				return SUCCESS;
			}
			else {
				System.out.println("here1");
				return LOGIN;	
			}
		}
		System.out.println("here2");
		return LOGIN;
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
	 * @return client
	 */
	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
	
	/**
	 * getter/setter
	 * @return AdminPrivBean
	 * @throws RemoteException
	 */
	public AdminPrivBean getAdminPrivBean() throws RemoteException{
		if(!session.containsKey("AdminPrivBean"))
			this.setAdminPrivBean(new AdminPrivBean());
		return (AdminPrivBean) session.get("AdminPrivBean");
	}

	public void setAdminPrivBean(AdminPrivBean AdminPrivBean){
		this.session.put("AdminPrivBean", AdminPrivBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
