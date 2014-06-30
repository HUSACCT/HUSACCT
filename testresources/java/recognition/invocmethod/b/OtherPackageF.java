package invocmethod.b;

public class OtherPackageF {

	private invocmethod.a.TheType ref = new invocmethod.a.TheType();
	
	public OtherPackageF(){
		ref.theMethod();
	}
	
}
