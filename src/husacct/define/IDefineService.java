package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IObservableService;

import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public interface IDefineService extends ISaveable, IObservableService {

    public void analyze();

    public void createApplication(String name, ArrayList<ProjectDTO> projects,
	    String version);

    public ApplicationDTO getApplicationDetails();

    public ModuleDTO[] getChildrenFromModule(String logicalPath);

    public JInternalFrame getDefinedGUI();

    public RuleDTO[] getDefinedRules();

    public Element getLogicalArchitectureData();

    public String getParentFromModule(String logicalPath);

    public ModuleDTO[] getRootModules();

    public boolean isDefined();

    public boolean isMapped();

    public void isReanalyzed();

    public void loadLogicalArchitectureData(Element e);
}
