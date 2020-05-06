package actions;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.LoginBean;

public class LoginAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String password = null;
	private String admin=null;

	@Override
	public String execute() throws RemoteException{	
		if(this.username != null && !username.equals("")) {	
			//store client info in session
			this.getLoginBean().setUsername(this.username);
			this.getLoginBean().setPassword(this.password);	

			if(getLoginBean().loginBean()) {

				this.admin=this.getLoginBean().getAdmin();
				
				session.put("username", username);
				session.put("password", password);
				session.put("adminPrivSent", false);
				
				if(session.containsKey("admin")) {
					session.remove("admin");
				}
				if(this.admin.equals("yes"))
					session.put("admin", true);
				else
					session.put("admin", false);
				
				if(session.containsKey("logged")) {
					session.remove("logged");
				}
				session.put("logged", true); 
				return SUCCESS;	
			}
			return LOGIN;			
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
	 * @return admin
	 */
	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/**
	 * getter/setter
	 * @return LoginBean
	 * @throws RemoteException
	 */
	public LoginBean getLoginBean() throws RemoteException{
		if(!session.containsKey("LoginBean"))
			this.setLoginBean(new LoginBean());
		return (LoginBean) session.get("LoginBean");
	}

	public void setLoginBean(LoginBean LoginBean){
		this.session.put("LoginBean", LoginBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}

}
