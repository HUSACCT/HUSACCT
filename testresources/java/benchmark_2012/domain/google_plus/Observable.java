package domain.google_plus;

import java.util.ArrayList;
import java.util.List;

import presentation.gui.observer.google_plus.Observer;

public abstract class Observable {
	private List<presentation.gui.observer.google_plus.Observer> observers = new ArrayList<Observer>();

	public void attach(presentation.gui.observer.google_plus.Observer o){
		observers.add(o);
	}

	public void detach(Observer o){
		observers.remove(o);
	}	

	public void notifyObservers(){
		for(Observer o : observers){
			o.update();
		}
	}
}