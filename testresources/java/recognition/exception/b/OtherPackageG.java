package exception.b;

public class OtherPackageG {

	public void doSomething() throws exception.a.TheException{
		throw new exception.a.TheException();
	}
	
}
