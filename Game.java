
public class Game {
	
	private int id; 
	private Gameboard round1Board;
	private Gameboard round2Board;
	private Question FJquestion;
	
	public Game(Gameboard b1, Gameboard b2, int x, Question q1) {
		round1Board = b1;
		round2Board = b2;
		id = x;
		FJquestion = q1;
	}

	public void writeGameToDatabase() {
		//TODO: implement with Justin
	}
	
	public void cleanUp() {
		for (Question q : round1Board.getQuestions()) {
			q.cleanUp();
		}
		for (Category c : round1Board.getCategories()) {
			c.cleanUp();
		}
		for (Question q : round2Board.getQuestions()) {
			q.cleanUp();
		} 
		for (Category c : round2Board.getCategories()) {
			c.cleanUp();
		}
		if (hasFinalJeopardy()) {
			FJquestion.cleanUp();
		}
	}
	
	public int getId() {
		return id;
	}
	
	public Gameboard getGame1() {
		return round1Board;
	}
	
	public Gameboard getGame2() {
		return round2Board;
	}
	
	public boolean hasFinalJeopardy() {
		return FJquestion != null;
	}
	
	public Question getFJquestion() {
		return FJquestion;
	}

}
