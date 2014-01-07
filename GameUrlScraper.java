
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GameUrlScraper {
	
	private ArrayList<String> gameIds;
	
	public GameUrlScraper() {
		gameIds = new ArrayList<String>();
		String seasonBase = "http://www.j-archive.com/showseason.php?season=";
		//get all game links for each season
		for (int i = 1; i < 31; i++) {
			String seasonLink = seasonBase + i;
			getGameLinks(seasonLink);
		}
	}
	
	public ArrayList<String> getGameIds() {
		return gameIds;
	}
	
	private void getGameLinks(String seasonLink) {
		URL url;
		String html = "";
		BufferedReader incomingHTML;
		
		try {
			url = new URL(seasonLink);
			try {
	           	incomingHTML = new BufferedReader(new InputStreamReader(url.openStream()));
	           	String currLine;
	            currLine = incomingHTML.readLine();
	            StringBuilder str = new StringBuilder();
	            while (currLine != null) {
	            	str.append(currLine);
	                currLine = incomingHTML.readLine();
	            }
	            incomingHTML.close();
	            html = str.toString();
	        }
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Pattern p = Pattern.compile("<a href=\".*?game_id=([0-9]+)\">");
        Matcher m = p.matcher(html);

        while (m.find()) {
        	gameIds.add(m.group(1));
        }
	}

}
