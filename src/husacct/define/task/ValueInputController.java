package husacct.define.task;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class ValueInputController {
	
	private String errorMessage = "";
	
	public ValueInputController() {
		
	}
	
	public boolean checkModuleNameInput(String nameInput) {
		if(isNotEmpty(nameInput, "hierarchical level")) {
			return true;
		}
		return false;
	}
	
	public boolean checkHierarchicalLevelInput(String levelInput) {
		if(isNotEmpty(levelInput, "hierarchical level") && isInteger(levelInput, "hierarchical level")){
			return true;
		}
		return false;
	}
	
	private boolean isNotEmpty(String input, String name) {
		if(input.isEmpty()) {
			this.errorMessage = "Please fill in a " + name;
			return false;
		}
		return true;
	}
	
	private boolean isInteger(String input, String name) {  
	   try {  
	      Integer.parseInt(input);  
	      return true;  
	   }  
	   catch(Exception e) {
		   this.errorMessage = "Please fill in a " + name;
	      return false;  
	   }  
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
