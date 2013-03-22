package husacct.control.presentation.workspace.loaders;

import java.util.HashMap;

import javax.swing.JPanel;

public abstract class LoaderPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    abstract public boolean validateData();

    abstract public HashMap<String, Object> getData();
}