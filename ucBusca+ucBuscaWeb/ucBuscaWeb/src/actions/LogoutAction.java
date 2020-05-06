package actions;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.LogoutBean;

public class LogoutAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;
	
	@Override
	public String execute() throws RemoteException {		
		//find client username/password in session
		String user=this.session.get("username").toString();
		this.getLogoutBean().setUsername(user);
		
		String pass=this.session.get("password").toString();
		this.getLogoutBean().setPassword(pass);
				
		//update login status in session
		if(getLogoutBean().logoutBean()) {			
			session.remove("logged");
			session.put("logged", false);
			return SUCCESS;
		}
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
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * getter/setter
	 * @return LogoutBean
	 * @throws RemoteException
	 */
	public LogoutBean getLogoutBean() throws RemoteException{
		if(!session.containsKey("LogoutBean"))
			this.setLogoutBean(new LogoutBean());
		return (LogoutBean) session.get("LogoutBean");
	}

	public void setLogoutBean(LogoutBean LogoutBean){
		this.session.put("LogoutBean", LogoutBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
