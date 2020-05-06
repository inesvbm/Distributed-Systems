package actions;

import com.opensymphony.xwork2.ActionSupport;

import model.RegistryBean;

import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class RegistryAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;

	@Override
	public String execute() throws RemoteException{
		if(this.username != null && !username.equals("")) {
			this.getRegistryBean().setUsername(this.username);
			this.getRegistryBean().setPassword(this.password);	
			if(getRegistryBean().registryBean()) {
				session.put("registered", true); 
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
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username){
		this.username = username; 
	}

	/**
	 * getter/setter
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password){
		this.password = password;  
	}
	
	/**
	 * getter/setter
	 * @return RegistryBean
	 * @throws RemoteException
	 */
	public RegistryBean getRegistryBean() throws RemoteException{
		if(!session.containsKey("RegistryBean"))
			this.setRegistryBean(new RegistryBean());
		return (RegistryBean) session.get("RegistryBean");
	}

	public void setRegistryBean(RegistryBean RegistryBean){
		this.session.put("RegistryBean", RegistryBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
