package ucBusca;
/**
 * 
 * @author inesv
 *
 */
public class ClientRequest {
	RMI_interface_client_methods client;
	String request_id;
	String username;
	int logged; //=1 if yes and =0 if not 
	
	
	ClientRequest(String request_id, String username, RMI_interface_client_methods client){
		this.client=client;
		this.username=username;
		this.request_id=request_id;	
		this.logged=0;
	}
}
