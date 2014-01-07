import java.util.ArrayList;


public class Gameboard {
	
	private ArrayList<Category> categories;
	private ArrayList<Question> questions;
	private boolean doubleJeopardy;
	
	public Gameboard(boolean b) {
		doubleJeopardy = b;
		categories = new ArrayList<Category>();
		questions = new ArrayList<Question>();
	}
	
	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}
	
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public ArrayList<Category> getCategories() {
		return categories;
	}
}
