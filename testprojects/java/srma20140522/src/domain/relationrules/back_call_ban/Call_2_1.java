package domain.relationrules.back_call_ban;

import technology.relationrules.allowed.SocialNetwork;

public class Call_2_1 {

	private SocialNetwork socialnetwork;
	
	public Call_2_1(){
		System.out.println(socialnetwork.getSocialNetworkType());
	}
}