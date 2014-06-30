package presentation.gui.orkut;

import domain.orkut.Comment;

//Functional requirement 3.1.2
//Test case 69: Only class presentation.gui.orkut.CommentGUI may have a dependency with domain.orkut.Comment
//Result: TRUE
public class CommentGUI {
	//FR5.5
	private Comment comment;
	
	public CommentGUI(){
		//FR5.1
		System.out.println(comment.getCommentType());
	}
}