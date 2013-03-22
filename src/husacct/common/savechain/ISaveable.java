package husacct.common.savechain;

import org.jdom2.Element;

public interface ISaveable {

    Element getWorkspaceData();

    void loadWorkspaceData(Element workspaceData);
}
