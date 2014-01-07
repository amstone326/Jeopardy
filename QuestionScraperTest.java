import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class QuestionScraperTest {
	
	private QuestionScraper qs;
	
	@Before
	public void setUp() {
		qs = new QuestionScraper();
	}

	@Test
	public void testGame1() {
		Game g = qs.getGames().get(0);
		System.out.println("Game Id: " + g.getId());
		System.out.println();
		Gameboard round1 = g.getGame1();
		ArrayList<Question> round1Questions = round1.getQuestions();
		//assertEquals(26, round1Questions.size());
		for (Question q : round1Questions) {
			System.out.println(q.getQuestion());
			System.out.println(q.getAnswer());
			System.out.println(q.getCategory().getName());
			System.out.println(q.getCategory().getComment());
			System.out.println("row: " + q.getRow() + ", col: " + q.getCol());
			System.out.println();
		}
		
		for (Category c : round1.getCategories()) {
			System.out.println(c.getName() + "'s questions are:");
			for (Question q : c.getQuestions()) {
				System.out.println(q.getQuestion());
			}
			System.out.println();
		}
		
		Gameboard round2 = g.getGame2();
		ArrayList<Question> round2Questions = round2.getQuestions();
		assertEquals(25, round2Questions.size());
		for (Question q : round2Questions) {
			System.out.println(q.getQuestion());
			System.out.println(q.getAnswer());
			System.out.println(q.getCategory().getName());
			System.out.println("row: " + q.getRow() + ", col: " + q.getCol());
			System.out.println();
		}
	}
	



}
