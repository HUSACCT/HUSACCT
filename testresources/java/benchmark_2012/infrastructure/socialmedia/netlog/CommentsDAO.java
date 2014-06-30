package infrastructure.socialmedia.netlog;

import infrastructure.socialmedia.SocialNetwork;
//Functional requirement 3.2.1
//Test case 160: All classes in package infrastructure.socialmedia.netlog are not allowed to use modules in a higher layer 
//Result: TRUE
public class CommentsDAO {
	//FR5.5
	private SocialNetwork socialnetwork;
	
	public CommentsDAO(){
		//FR5.2
		System.out.println(socialnetwork.type);
	}
}