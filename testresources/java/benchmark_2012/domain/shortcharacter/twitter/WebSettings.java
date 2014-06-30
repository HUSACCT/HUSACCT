package domain.shortcharacter.twitter;

import infrastructure.socialmedia.SocialNetworkInfo;

import java.util.ArrayList;

//Functional requirement 3.1.1
//Test case 60: Class domain.shortcharacter.twitter.Websettings may only have a dependency with SocialNetworkInfo
//Result: TRUE
public class WebSettings {
	public WebSettings(){
		char[] chararray = SocialNetworkInfo.getInfo().toCharArray();
		ArrayList<String> stringList = new ArrayList<String>();
		for(char c :chararray){
			stringList.add(Character.toString(c));
		}
	}
}