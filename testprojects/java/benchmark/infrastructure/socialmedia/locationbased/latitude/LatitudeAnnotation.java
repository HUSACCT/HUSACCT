package infrastructure.socialmedia.locationbased.latitude;
//Functional requirement 3.2
//Test case 133: Class domain.locationbased.latitude.Settings is not allowed to use annotation infrastructure.socialmedia.locationbased.foursquare.FourSquareAnnotation
//Result: TRUE
public @interface LatitudeAnnotation {
	public String author() default "Themaopdracht 7 tester";
}