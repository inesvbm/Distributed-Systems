package ucBusca;

import java.io.Serializable;

/**
 * 
 * @author inesv
 *
 */
public class SearchNumber implements Serializable{
	private static final long serialVersionUID = 1L;
	String text; //client search
	int count; //number of search occurrences 
	
	public SearchNumber(String text, int count){
		this.text=text;
		this.count=count;
	}
}
