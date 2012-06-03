package domain.hyves;

import java.util.Observable;

public class Krabbel extends Observable {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {		
		this.text = text;
		setChanged();
		notifyObservers(text);
	}
}