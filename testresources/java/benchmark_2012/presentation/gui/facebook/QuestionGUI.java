package presentation.gui.facebook;

import domain.facebook.FacebookAnnotation;

//Functional requirement 3.1.2
//Test case 84: Only class presentation.gui.facebook.QuestionGUI may have a dependency with domain.facebook.FacebookAnnotation
//Result: FALSE

//FR5.6
@FacebookAnnotation(facebookId = "1234567890")
public class QuestionGUI {
}