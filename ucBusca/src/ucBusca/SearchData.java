package ucBusca;

import java.io.Serializable;
import java.util.HashSet;
/**
 * 
 * @author inesv
 *
 */
public class SearchData implements Serializable{
	private static final long serialVersionUID = 1L;
	String title;
	String description;
	String url;
	HashSet<String> linksAssoc;
	
	public SearchData(String title, String description, String url, HashSet<String> linksAssoc){
		this.title=title;
		this.description=description;
		this.url=url;
		this.linksAssoc=linksAssoc;
	}
}
