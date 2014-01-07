public class Question {
	
	private String question;
	private Category category;
	private String answer;
	private int rowy;
	private int colx;
	private int value;
	private boolean hasLink;
	private boolean doubleJeopardy;
	
	public Question(String s, int row, int col) {
		question = s;
		rowy = row;
		colx = col;
		hasLink = false;
		category = null;
		answer = "";
		value = 0;
		doubleJeopardy = false;
	}
	
	public void cleanUp() {
		question = question.replace("&amp;", "and");
		answer = answer.replace("&amp;", "and");
		question = question.replace("&lt;i&gt;", "");
		question = question.replace("&lt;/i&gt;", "");
		answer = answer.replace("&lt;i&gt;", "");
		answer = answer.replace("&lt;/i&gt;", "");
		question = question.replace("&quot;", "\"");
		answer = answer.replace("&quot;", "\"");
		if (question.contains("href") || answer.contains("href")) {
			hasLink = true;			
		}
	}
	
	public int getCol() {
		return colx;
	}
	
	public int getRow() {
		return rowy;
	}

	public String getQuestion() {
		return question;
	}
	
	public void setRoundAndValue(boolean b) {
		doubleJeopardy = b;
		value = rowy*200;
		if (doubleJeopardy) {
			value*=2;
		}
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category c) {
		category = c;
	}
	
	public void setAnswer(String s) {
		answer = s;
	}
	
	public void writeToDatabase() {
		//TODO: implement
	}
	
}
