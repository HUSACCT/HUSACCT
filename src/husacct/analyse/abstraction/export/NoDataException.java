package husacct.analyse.abstraction.export;

public class NoDataException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NoDataException(){
		super("Could not export analysed data to a file, because no data is available");
	}
	
}
