package infrastructure.socialmedia.locationbased.foursquare;
//Functional requirement 3.2
//Test case 134: Class domain.locationbased.foursquare.Settings is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.FourSquareAnnotation
//Result: FALSE
public @interface FourSquareAnnotation {
	public String author() default "Themaopdracht 7 tester";
}