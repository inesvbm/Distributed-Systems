package ucBusca;

import java.io.Serializable;
import java.util.HashSet;
/**
 * 
 * @author inesv
 *
 */
public class PageData implements Serializable{
	private static final long serialVersionUID = 1L;
	String name;
	String title;
	String description;
	HashSet<String> linksAssoc; //associated links from URL (name)
	HashSet<String> connections_to=new HashSet<String>(); //connections for URL (name)
	int count_links=0; //connections_to.size
	
	public PageData(String title, String description, String name, HashSet<String> linksAssoc){
		this.title=title;
		this.description=description;
		this.name=name;
		this.linksAssoc=linksAssoc;
	}
	public PageData(String title, String description, String name, int count_links){
		this.title=title;
		this.description=description;
		this.name=name;
		this.count_links=count_links;
	}
	
}
