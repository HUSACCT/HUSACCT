package husacct.define.task.components;

import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.ArrayList;

public class RegexComponent extends AnalyzedModuleComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 4392727725986533362L;
    private AnalyzedModuleComponent excludedModules = new AnalyzedModuleComponent(
	    "root", "Excluded Modules", "SEARCH", "public");
    private ArrayList<AbstractCombinedComponent> softwareUnit = new ArrayList<AbstractCombinedComponent>();
    private String type = "";
    private String uniqueName = "";
    private String visibility;

    private AnalyzedModuleComponent wrapper;

    public RegexComponent() {
	super();
    }

    public RegexComponent(String uniqueName, String name, String type,
	    String visibility) {
	this();
	setUniqueName(uniqueName);
	setName(name);
	setType(type);
	setVisibility(visibility);
    }

    @Override
    public void addChild(AbstractCombinedComponent child) {

	softwareUnit.add(child);
    }

    @Override
    public ArrayList<AbstractCombinedComponent> getChildren() {
	return softwareUnit;
    }

    @Override
    public String getType() {
	return type;
    }

    @Override
    public String getUniqueName() {
	return uniqueName;
    }

    @Override
    public String getVisibility() {
	return visibility;
    }

    public AnalyzedModuleComponent getWrapper() {
	return wrapper;
    }

    @Override
    public void removeChild(AbstractCombinedComponent child) {

	int index = softwareUnit.indexOf(child);
	if (index >= 0) {
	    softwareUnit.remove(index);
	}

    }

    @Override
    public void setChildren(ArrayList<AbstractCombinedComponent> children) {

    }

    @Override
    public void setType(String type) {
	this.type = type.toUpperCase();
    }

    @Override
    public void setUniqueName(String uniqueName) {
	this.uniqueName = uniqueName;
    }

    @Override
    public void setVisibility(String visibility) {
	this.visibility = visibility;
    }

    public void setWrapper(AnalyzedModuleComponent wrapper) {
	setUniqueName(wrapper.getUniqueName());
	setName(wrapper.getName());
	setType(wrapper.type);
	setChildren(wrapper.getChildren());
	this.wrapper = wrapper;
    }
    
    public void setRegex(ExpressionUnitDefinition unit )
    {
    	for (SoftwareUnitDefinition unite : unit.getExpressionValues()) {
			addChild(StateService.instance().getAnalyzedSoftWareUnit(unite));
		}
    	
    }

	public void setRegex(SoftwareUnitRegExDefinition regExSoftwareUnitByName) {
		// TODO Auto-generated method stub
		
	}

}
