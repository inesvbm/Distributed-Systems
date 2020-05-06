package ucBusca;

import java.io.Serializable;
/**
 * 
 * @author inesv
 *
 */
public class RegisteredClients implements Serializable{
	private static final long serialVersionUID = 1L;
	String username;
	String password;
	String admin;
	
	public RegisteredClients(String username, String password, String admin){
		this.username=username;
		this.password=password;
		this.admin=admin;
	}
}
