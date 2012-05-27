package infrastructure.socialmedia.locationbased.foursquare;
//Functional requirement 3.2
//Test case 138: Class domain.locationbased.foursquare.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSqaureException 
//Result: FALSE
public class FourSquareException extends Exception{
	public FourSquareException(String message){
		super(message);
	}
}