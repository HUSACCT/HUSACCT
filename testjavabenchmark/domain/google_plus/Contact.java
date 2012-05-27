package domain.google_plus;

public class Contact extends Observable {
	private String name;

	public Contact(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}