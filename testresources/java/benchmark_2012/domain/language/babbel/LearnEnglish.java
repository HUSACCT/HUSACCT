package domain.language.babbel;

import infrastructure.asocialmedia.ASocialNetworkInfo;

//Functional requirement 3.1.3
//Test case 97: Class domain.language.babbel.LearnEnglish must use class infrastructure.socialmedia.SocialNetworkInfo
//Result: True
public class LearnEnglish {
	public String sniName = "";

	public LearnEnglish(){
		//FR5.2
		//System.out.println(ASocialNetworkInfo.message);
	}
	public void testAccessStaticAttribute() {
		ASocialNetworkInfo.message = "klant";
	}
	
	public void testAccessStaticFinalAttribute() {
		sniName = ASocialNetworkInfo.name;
	}
}