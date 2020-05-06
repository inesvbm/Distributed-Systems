package ucBusca;
/**
 * 
 * @author inesv
 *
 */
public class MulticastData {
	String id;
	String MULTICAST_ADDRESS;
	int PORT_MULT;
	
	public MulticastData(String id, String MULTICAST_ADDRESS, int PORT_MULT){
		this.id=id;
		this.MULTICAST_ADDRESS=MULTICAST_ADDRESS;
		this.PORT_MULT=PORT_MULT;
	}
}
