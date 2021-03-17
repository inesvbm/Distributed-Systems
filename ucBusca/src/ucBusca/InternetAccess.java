package ucBusca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 
 * @author code from Inforestudante
 *
 */
public class InternetAccess {
	//this parameters will be used by class RequestHandler
	public static String title="";
	//description
	public static String text1="";
	//main links
	public static String mainLink="";
	//associated links
	public static HashSet<String> linksAssociated=new HashSet<String>();
	//words
	public static HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		
	public InternetAccess(String message){
		super();
	}
	
	//read client input
	public void readClientInput(String message){	
		countMap.clear();
		linksAssociated.clear();
        try {
            if (!message.startsWith("http://") && !message.startsWith("https://"))
            	message = "http://".concat(message);

            // Attempt to connect and get the document            
            Document doc = Jsoup.connect(message).get();  // Documentation: https://jsoup.org/
            mainLink=message;
            readDocument(doc);
            
            // Get website text and count words
            String text = doc.text(); // We can use doc.body().text() if we only want to get text from <body></body>
            countWords(text);
            
        } catch (IOException e) {
        	System.out.println("Could not read link");	
        } catch (Exception e) {
        	System.out.println("Could not read link");	
        }
	}
	
	public void readDocument(Document doc){
        // Title		
        title=doc.title();
        // Get all links
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            // Ignore bookmarks within the page
            if (link.attr("href").startsWith("#")) {
                continue;
            }
            // Shall we ignore local links? Otherwise we have to rebuild them for future parsing
            if (!link.attr("href").startsWith("http")) {
                continue;
            }
            linksAssociated.add(link.attr("href"));
            text1=link.text();
        } 
    }

	public void countWords(String text) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
        String line;

        // Get words and respective count
        while (true) {
            try {
                if ((line = reader.readLine()) == null)
                    break;
                String[] words = line.split("[ ,;:.?!“”(){}\\[\\]<>']+");
                for (String word : words) {
                    word = word.toLowerCase();
                    if ("".equals(word)) {
                        continue;
                    }
                    if (!countMap.containsKey(word)) {
                        countMap.put(word, 1);
                    }
                    else {
                        countMap.put(word, countMap.get(word) + 1);
                    }
                }
            } catch (IOException e) {
            	System.out.println("Could not read words");	
            }
        }

        // Close reader
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display words and counts
        for (String word : countMap.keySet()) {
            if (word.length() >= 3) { // Shall we ignore small words?
                //System.out.println(word + "\t" + countMap.get(word));
            }
        }
    }

}
