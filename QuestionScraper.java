import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuestionScraper {
	
	private ArrayList<Game> allGames;
	private Set<String> invalidGameIds;
		
	public QuestionScraper() {
		allGames = new ArrayList<Game>();
		addInvalidGames();
		GameUrlScraper scraper = new GameUrlScraper();
		String gameBase = "http://www.j-archive.com/showgame.php?game_id=";
		for (String s : scraper.getGameIds()) {
			if (invalidGameIds.contains(s)) {
				continue;
			}
			System.out.println("Last game Id tried: " + s);
			try {
				URL url = new URL(gameBase + s);
				Game g = createGame(url, s);
				g.cleanUp();
				allGames.add(g);
				g.writeGameToDatabase();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addInvalidGames() {
		invalidGameIds = new HashSet<String>();
		invalidGameIds.add("4284");
		invalidGameIds.add("4281");
		invalidGameIds.add("4279");
		invalidGameIds.add("4271");
		invalidGameIds.add("4273");
		invalidGameIds.add("4256");
		invalidGameIds.add("4261");
		invalidGameIds.add("295");
		invalidGameIds.add("3117");
		invalidGameIds.add("4246"); 
		invalidGameIds.add("4296"); 
		invalidGameIds.add("3973"); 
		invalidGameIds.add("4226"); 
		invalidGameIds.add("1243"); 
		invalidGameIds.add("2117"); 
		invalidGameIds.add("2356"); 
		invalidGameIds.add("1743"); 
		invalidGameIds.add("1742"); 
		invalidGameIds.add("1744"); 
		invalidGameIds.add("1745"); 
		invalidGameIds.add("1746"); 
		invalidGameIds.add("1747"); 
		invalidGameIds.add("1748"); 
		invalidGameIds.add("1749"); 
		invalidGameIds.add("1750"); 
		invalidGameIds.add("1751"); 
		invalidGameIds.add("1752"); 
		invalidGameIds.add("1753");
		invalidGameIds.add("3576");
		invalidGameIds.add("3575");
	}
	
	public ArrayList<Game> getGames() {
		return allGames;
	}
	
	private Game createGame(URL url, String stringId) {
		String html = "";
		BufferedReader incomingHTML;
		
		//Get html
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
				
		//Get rows and columns for both rounds
		Queue<Integer> rows = new LinkedList<Integer>();
		Queue<Integer> columns = new LinkedList<Integer>();
		Pattern p = Pattern.compile("<td id=\"clue_D?J_(.?)_(.?)_");
        Matcher m = p.matcher(html);
        while (m.find()) {
        	columns.offer(Integer.parseInt(m.group(1)));
        	rows.offer(Integer.parseInt(m.group(2)));
        }
        
		//Get categories 
		ArrayList<Category> round1Categories = new ArrayList<Category>();
		ArrayList<Category> round2Categories = new ArrayList<Category>();
		p = Pattern.compile("<td class=\"category_name\">(.*?)</td>");
        m = p.matcher(html);
        Pattern pComment = Pattern.compile("<td class=\"category_comments\">(.*?)</td>");
        Matcher mComment = pComment.matcher(html);
        Category FJcategory = null;
        int count = 0;
        while (m.find()) {
        	String comment = "";
        	if (mComment.find()) {
        		comment = mComment.group(1);
        	}
        	else {
        		System.out.println("FLAG: possible mismatched categories and comments");
        	}
        	Category c = new Category(m.group(1), comment);
        	if (count < 6) {
        		round1Categories.add(c);
        	}
        	else if (count < 12) {
        		round2Categories.add(c);
        	}
        	else if (count == 12) {
        		FJcategory = c;
        	}
        	else {
        		System.out.println("FLAG: extra categories present");
        	}
        	count++;
        }
        
        //Get questions
        p = Pattern.compile("clue_text\">(.*?)</td>");
        m = p.matcher(html);
        ArrayList<Question> allQuestions = new ArrayList<Question>();
        count = 0;
        int numQuestionsInRound1 = 0;
        int nextRow;
        int nextCol;
        while (!rows.isEmpty() && !columns.isEmpty()) {
        	if (m.find()) {
        		nextRow = rows.poll();
        		nextCol = columns.poll();
        		//PROBLEM: Won't work if first question is ever blank
        		if (count > 0 && nextRow == 1 && nextCol == 1) {
        			numQuestionsInRound1 = count;
        		}
        		Question q = new Question(m.group(1), nextRow, nextCol);
        		allQuestions.add(q);
        		count++;
        	}
        	else {
        		System.out.println("FLAG: not enough questions for rows/cols");
        	}
        }
        Question FJquestion;
        if (m.find()) {
        	FJquestion = new Question(m.group(1), -1, -1);
        	FJquestion.setCategory(FJcategory);
        }
        else {
        	FJquestion = null;
        	System.out.println("FLAG: no FJ question");
        }
         
        //Get answers 
        p = Pattern.compile("correct_response&quot;&gt;(.*?)&lt;/em&gt;");
        m = p.matcher(html);
        int index = 0;
        while (m.find() && index < allQuestions.size()) {
        	allQuestions.get(index).setAnswer(m.group(1));
        	index++;
        }
        
        /* TODO: Get FJ answer
        p = Pattern.compile("correct_response\\&quot;&gt;(.*?)&lt;/em&gt;");
        m = p.matcher(html);
        FJquestion.setAnswer(m.group(1));*/
        
        //Split questions by round
        ArrayList<Question> round1Questions = new ArrayList<Question>();
        ArrayList<Question> round2Questions = new ArrayList<Question>();
        for (int i = 0; i < allQuestions.size(); i++) {
        	if (i < numQuestionsInRound1) {
        		round1Questions.add(allQuestions.get(i));
        	}
        	else {
        		round2Questions.add(allQuestions.get(i));
        	}
        }
       
        //Set categories and create boards
        Gameboard jeopardyBoard = createBoard(round1Questions, round1Categories, false);
        Gameboard doubleJeopardyBoard = createBoard(round2Questions, round2Categories, true);

        //Create this game with its gameboards 
        Game g = new Game(jeopardyBoard, doubleJeopardyBoard, Integer.parseInt(stringId), FJquestion);
        return g;
	}
	
	private Gameboard createBoard(ArrayList<Question> questions, ArrayList<Category> categories, 
			boolean djBoolean) {
		
		for (Question q : questions) {
			q.setRoundAndValue(djBoolean);
			Category thisC = categories.get(q.getCol()-1);
        	q.setCategory(thisC);
			thisC.addQuestion(q);
		}	
		Gameboard board = new Gameboard(djBoolean);
		board.setCategories(categories);
		board.setQuestions(questions);
		return board;
	}

}