package presentation.post;

import infrastructure.socialmedia.SocialNetworkInfo;
//Functional requirement 3.2.2
//Test case 166: The classes in package presentation.post are not allowed to use modules in a not direct lower layer
//Result: FALSE
public class PostHyvesMessage {
	public PostHyvesMessage(){
		//FR5.2
		System.out.println(SocialNetworkInfo.message);
	}
}