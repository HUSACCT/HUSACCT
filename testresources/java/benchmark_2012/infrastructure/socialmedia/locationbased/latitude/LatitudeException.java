package infrastructure.socialmedia.locationbased.latitude;
//Functional requirement 3.2
//Test case 137: Class domain.locationbased.latitude.Statics is not allowed to use exception infrastructure.socialmedia.locationbased.foursquare.FourSquareException
//Result: TRUE
public class LatitudeException extends Exception{
	public LatitudeException(String message){
		super(message);
	}
}