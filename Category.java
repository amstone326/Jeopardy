import java.util.ArrayList;


public class Category {
	
	private String name;
	private String comment;
	private ArrayList<Question> questions;
	private boolean hasLink;
	
	public Category (String s1, String s2) {
		name = s1;
		comment = s2;
		questions = new ArrayList<Question>();
		hasLink = false;
	}
	
	public String getComment() {
		return comment;
	}
	
	public boolean hasComment() {
		return comment != "";
	}
	
	public void addQuestion(Question q) {
		questions.add(q);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public void cleanUp() {
		name = name.replace("&amp;", "and");
		name = name.replace("&lt;i&gt;", "");
		name = name.replace("&lt;/i&gt;", "");
		name = name.replace("&quot;", "\"");
		if (name.contains("href")) {
			hasLink = true;			
		}
	}

}
