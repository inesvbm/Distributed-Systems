package actions;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.SearchBean;

public class TranslateAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private ArrayList<ArrayList<String>> result = null;	
	private ArrayList<String> translated=null;

	public String execute() throws IOException {
		//this.result=this.getSearchBean().getResult();
		this.result=(ArrayList<ArrayList<String>>)(this.session.get("result"));
		translated=new ArrayList<String>();

		for(ArrayList<String> array: result) {
			this.getSearchBean();
			String language=SearchBean.checkLanguage(array.get(0));		
			String trans=SearchBean.translate(array.get(0), language);
			System.out.println(trans);
			this.translated.add(trans);
		}		
		return SUCCESS;
	}
	
	public ArrayList<String> getTranslated() {
		return translated;
	}

	public void setTranslated(ArrayList<String> translated) {
		this.translated = translated;
	}

	public ArrayList<ArrayList<String>> getResult() {
		return result;
	}

	public void setResult(ArrayList<ArrayList<String>> result) {
		this.result = result;
	}

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
	public void setSession(Map<String, Object> session){
		this.session = session;
	}
}
