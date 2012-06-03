package presentation.legal;

import infrastructure.socialmedia.twitter.LegalInformation;

//Functional requirement 4.2
//Test case 193: Class presentation.legal.TwitterLegal is not allowed to use the classes in package infrastructure.socialmedia.twitter except for class infrastructure.socialmedia.twitter.TwitterFacade
//Result: TRUE
public class TwitterIlLegal {
	private LegalInformation legalInformation;
	
	public TwitterIlLegal(){
		this.legalInformation = new LegalInformation();
	}
	
	public String getTermsOfService(){
		return legalInformation.getTermsOfService();
	} 

	public String getPrivacyPolicy(){
		return legalInformation.getPrivacyPolicy();
	}
}