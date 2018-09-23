package husaccttest;

import husacct.control.task.WorkspaceController;

public class TestUtils {
    private TestUtils() {
    }

    public static void closeWorkspace(WorkspaceController controller) {
        if (controller.getCurrentWorkspace() != null) {
            controller.getCurrentWorkspace().clearDirty();
        }
        controller.closeWorkspace();
    }
}
