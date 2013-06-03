package husacct.define.task.conventions_checker;

import husacct.define.task.JtreeController;
import husacct.define.task.components.AnalyzedModuleComponent;

public class AnalyzedComponentHelper {

    public AnalyzedComponentHelper() {

    }

    public void chekIfDataIsTheSame(AnalyzedModuleComponent rootComponent) {
	if (JtreeController.instance().getTree() != null) {
	    chekmenow(rootComponent);

	}

	else {

	}

    }

    private void chekmenow(AnalyzedModuleComponent rootComponent) {

	// ArrayList<AnalyzedModuleComponent>

    }

}
