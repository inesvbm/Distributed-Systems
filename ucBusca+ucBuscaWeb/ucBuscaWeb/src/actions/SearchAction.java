package actions;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.SearchBean;

public class SearchAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String search = null;
	private String username=null;
	private ArrayList<ArrayList<String>> result = null;
	
	@Override
	public String execute() throws IOException{
		String user="";
		if(this.search != null && !search.equals("")) {			
			this.getSearchBean().setSearch(this.search);
			
			//manage type of search (with/without client username)
			if(session.containsKey("username")) {
				user=this.session.get("username").toString();
				this.getSearchBean().setUsername(user);
			}
			else {
				this.getSearchBean().setUsername("none");		
			}			
			if(getSearchBean().searchBean()) {
				this.getSearchBean().setSearch(this.search);
				this.result=this.getSearchBean().getResult();
				session.put("found", true);
				session.put("search", search);
				session.put("result", result);
								
				for(ArrayList<String> array: result) {
					this.getSearchBean();
					String language=SearchBean.checkLanguage(array.get(0));
					array.add(language);
				}						
				return SUCCESS;	
			}
			else {
				session.put("found", false);
				return SUCCESS;	
			}
		}
		else
			return LOGIN;
	}


	/**
	 * getter/setter
	 * @return username
	 */
	public String getUsername(){
		return username;  
	}
	
	public void setUsername(String username){
		this.username = username;  
	}
	
	/**
	 * getter/setter
	 * @return search
	 */
	public String getSearch() {
		return search;
	}

	public void setSearch(String search){
		this.search = search;  
	}
	
	/**
	 * getter/setter
	 * @return search results
	 */
	public ArrayList<ArrayList<String>> getResult(){
		return result;  
	}

	public void setResult(ArrayList<ArrayList<String>> result){
		this.result = result;  
	}
	
	/**
	 * 
	 * @return SearchBean
	 * @throws RemoteException
	 */
	public SearchBean getSearchBean() throws RemoteException{
		if(!session.containsKey("SearchBean"))
			this.setSearchBean(new SearchBean());
		return (SearchBean) session.get("SearchBean");
	}

	public void setSearchBean(SearchBean SearchBean){
		this.session.put("SearchBean", SearchBean);
	}

	/**
	 * session setter
	 */
	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
