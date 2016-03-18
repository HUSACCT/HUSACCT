package domain.relationrules.back_call_ban.exception;

import presentation.relationrules.notallowed.allowed.ASocialNetwork3;

public class ViolatingAccess_2_1Exc {
	private ASocialNetwork3 asocialnetwork;
	public ViolatingAccess_2_1Exc(){
		System.out.println(asocialnetwork.type);
	}
}