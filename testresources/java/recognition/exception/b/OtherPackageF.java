package exception.b;

import exception.a.*;

public class OtherPackageF {

	public void doSomething() throws TheException{
		throw new TheException();
	}
	
}
