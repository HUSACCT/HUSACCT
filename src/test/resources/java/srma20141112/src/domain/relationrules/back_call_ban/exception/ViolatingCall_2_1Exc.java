package domain.relationrules.back_call_ban.exception;

import presentation.relationrules.notallowed.allowed.ASocialNetwork3;

public class ViolatingCall_2_1Exc {
	private ASocialNetwork3 asocialnetwork;

	public ViolatingCall_2_1Exc(){
		System.out.println(asocialnetwork.getASocialNetworkType());
	}
}