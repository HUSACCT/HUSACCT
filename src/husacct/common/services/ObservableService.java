package husacct.common.services;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ObservableService {
	
	private Logger logger = Logger.getLogger(ObservableService.class);
	private ArrayList<IServiceListener> listeners = new ArrayList<IServiceListener>();
	
	public void addServiceListener(IServiceListener listener){
		listeners.add(listener);
	}
	
	public void notifyServiceListeners(){
		// Copy the current listeners to avoid ConcurrentModificationExceptions
		// Usually triggered when a listener is added while notifying the listeners
		@SuppressWarnings("unchecked")
		ArrayList<IServiceListener> listenersCopy = (ArrayList <IServiceListener>) this.listeners.clone();
		
		for(IServiceListener listener : listenersCopy){
			logger.debug("Notifying: " + listener.getClass());
			listener.update();
		}
	}
}
