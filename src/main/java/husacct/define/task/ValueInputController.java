package husacct.define.task;

public class ValueInputController {

    private String errorMessage = "";

    public ValueInputController() {

    }

    public boolean checkModuleNameInput(String nameInput) {
	if (isNotEmpty(nameInput, "module name")) {
	    return true;
	}
	return false;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    private boolean isNotEmpty(String input, String name) {
	if (input.isEmpty()) {
	    errorMessage = "Please fill in a " + name;
	    return false;
	}
	return true;
    }
}
