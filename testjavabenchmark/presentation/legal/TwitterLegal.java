package presentation.legal;

import infrastructure.socialmedia.twitter.TwitterFacade;
//Functional requirement 4.2
//Test case 193: Class presentation.legal.TwitterLegal is not allowed to use the classes in package infrastructure.socialmedia.twitter except for class infrastructure.socialmedia.twitter.TwitterFacade
//Result: TRUE
public class TwitterLegal {
	private TwitterFacade twitter;
	
	public TwitterLegal(){
		twitter = new TwitterFacade();
	}
	
	public String getTermsOfService(){
		return twitter.getTermsOfService();
	}
	
	public String getPrivacyPolicy(){
		return twitter.getTermsOfService();
	}
}