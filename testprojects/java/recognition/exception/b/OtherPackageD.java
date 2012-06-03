package exception.b;

import exception.a.TheException;

public class OtherPackageD {

	public void doSomething() throws TheException{
		throw new TheException();
	}
	
}
