package actions;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.NotificationsBean;

public class NotificationsAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null;
	private String message = null;
	private String clientUsername = null;
	
	/**
	 * verify if client has a notification
	 */
	@Override
	public String execute() throws RemoteException{		
		if(getNotificationsBean().notificationsBean()) {
			
			//check client username in session
			String user=this.session.get("username").toString();
			this.getNotificationsBean().setUsername(user);

			//get notification for client
			this.message=this.getNotificationsBean().getMessage();
			String[] aux=message.split("----");
			this.clientUsername=aux[0];
						
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
	 * @return notification for client
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * getter/setter 
	 * @return clientUsername
	 */
	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}

	/**
	 * getter/setter
	 * @return NotificationsBean
	 * @throws RemoteException
	 */
	public NotificationsBean getNotificationsBean() throws RemoteException{
		if(!session.containsKey("NotificationsBean"))
			this.setNotificationsBean(new NotificationsBean());
		return (NotificationsBean) session.get("NotificationsBean");
	}

	public void setNotificationsBean(NotificationsBean NotificationsBean){
		this.session.put("NotificationsBean", NotificationsBean);
	}

	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
