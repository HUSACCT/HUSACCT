package domain.facebook;

//Functional requirement 3.1.2
//Test case 84: Only class presentation.gui.facebook.QuestionGUI may have a dependency with domain.facebook.FacebookAnnotation
//Result: FALSE
public @interface FacebookAnnotation {
	public String facebookId();
}
