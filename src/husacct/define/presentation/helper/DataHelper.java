package husacct.define.presentation.helper;

public class DataHelper {
	private long id;
	private String value;

	public void setId(long id) {
		this.id = id;
	}
	
	public long getId(){
		return id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return getValue();
	}

}
