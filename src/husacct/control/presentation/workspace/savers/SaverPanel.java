package husacct.control.presentation.workspace.savers;

import java.util.HashMap;

import javax.swing.JPanel;

public abstract class SaverPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    abstract public boolean validateData();

    abstract public HashMap<String, Object> getData();
}