package domain.google_plus;

import java.util.ArrayList;
import java.util.List;

public class Circle extends Observable {
	private List<Contact> contacts;

	public Circle(){
		contacts = new ArrayList<Contact>();
	}

	public void addContact(Contact contact){
		contacts.add(contact);
	}

	public void removeContact(Contact contact){
		contacts.remove(contact);
	}
}