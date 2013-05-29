package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;


public class StateDefineController {
	private int currentIndex = -1;
	private boolean islocked= false;
	private boolean[] temp= new boolean[]{false,false};
	
	private ArrayList<Istate> states = new ArrayList<Istate>();

	public boolean undo() {
		int size =states.size();
		int var = size*size;
		islocked=true;
       int chek =currentIndex*size;
   
		
        if (currentIndex*size>=0) {
		states.get(currentIndex).undo();
		currentIndex--;
		
        }else if (currentIndex*size==var) {
        	currentIndex--;
        	states.get(currentIndex).undo();
		}else if (chek<0) {
			return false;
		}
     
        
        return currentIndex!=-1;
		
	}

	public boolean redo() {
		int size =states.size();
		int var = size*size;
		islocked=true;
		boolean answer =false; 
	
	 if (currentIndex==-1) {
		currentIndex=0;
		temp= new boolean[]{true,true};
		states.get(currentIndex).redo();
		
		return true;
	}	
	else if (currentIndex*size<var) {
			currentIndex++;
		states.get(currentIndex).redo();
	
	}
	 
	 if (currentIndex*size!=var) {
		answer = false;
		
		
	}



		
	return answer;
	

	}

	public void insertCommand(Istate sate) {
	
		 if (!islocked&& currentIndex+1!=states.size()) {
			removeStates();
			currentIndex++; 
		
			states.add(sate);
		} else if (!islocked) {
			currentIndex++; 
			
			states.add(sate);
		     
		}

	}

	private void removeStates() {
		
		for (int i = this.currentIndex+1; i < states.size(); i++) {
			states.remove(i);
		}
		
	}

	public void unlock() {
		islocked=false;
		
	}
	
	public boolean[] getStatesStatus()
	{
		
	
	
			
		
		if (states.size()==0) {
			temp= new boolean[]{false,false};
		}else if (currentIndex==0) {
			temp= new boolean[]{false,true};
		
		
		}else if (currentIndex+1==states.size()) {
			temp= new boolean[]{true,false};
		}else if (currentIndex<states.size()) {
			temp= new boolean[]{true,true};
		}else if (currentIndex>states.size()) {
			temp= new boolean[]{true,false};
		}
	
	 return temp;
		
		
		
		
	}

}
