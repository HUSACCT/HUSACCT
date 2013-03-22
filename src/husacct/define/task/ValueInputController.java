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

    private boolean isNotEmpty(String input, String name) {
        if (input.isEmpty()) {
            this.errorMessage = "Please fill in a " + name;
            return false;
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
