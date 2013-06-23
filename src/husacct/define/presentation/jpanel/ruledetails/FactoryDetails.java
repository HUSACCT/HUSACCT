package husacct.define.presentation.jpanel.ruledetails;

import husacct.define.presentation.jpanel.ruledetails.contentsmodule.FacadeConventionRuleJPanel;
import husacct.define.presentation.jpanel.ruledetails.contentsmodule.InterfaceConventionJPanel;
import husacct.define.presentation.jpanel.ruledetails.contentsmodule.NamingConventionExceptionJPanel;
import husacct.define.presentation.jpanel.ruledetails.contentsmodule.NamingConventionJPanel;
import husacct.define.presentation.jpanel.ruledetails.contentsmodule.SuperClassInheritanceConventionJPanel;
import husacct.define.presentation.jpanel.ruledetails.contentsmodule.VisibilityConventionExceptionJPanel;
import husacct.define.presentation.jpanel.ruledetails.contentsmodule.VisibilityConventionJPanel;
import husacct.define.presentation.jpanel.ruledetails.dependencylimitation.CyclesBetweenModulesExceptionJPanel;
import husacct.define.presentation.jpanel.ruledetails.dependencylimitation.CyclesBetweenModulesJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.BackCallJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.IsAllowedToUseJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.IsNotAllowedToUseJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.IsOnlyAllowedToUseJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.IsOnlyModuleAllowedToUseJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.MustUseJPanel;
import husacct.define.presentation.jpanel.ruledetails.legalitydependency.SkipCallJPanel;
import husacct.define.task.AppliedRuleController;

import org.apache.log4j.Logger;

public class FactoryDetails {

    private BackCallJPanel backCallJPanel;
    // Rules on the Dependency Limitation
    private CyclesBetweenModulesJPanel cyclesBetweenModules;
    private CyclesBetweenModulesExceptionJPanel cyclesBetweenModulesExceptionJPanel;
    private FacadeConventionRuleJPanel facadeConventionJPanel;
    // Rules on the Contents of a module
    private InterfaceConventionJPanel interfaceConventionJPanel;
    private IsAllowedToUseJPanel isAllowedToUseJPanel;
    // Rules of the Legality of Dependency
    private IsNotAllowedToUseJPanel isNotAllowedToUseJPanel;
    private IsOnlyAllowedToUseJPanel isOnlyAllowedToUseJPanel;
    private IsOnlyModuleAllowedToUseJPanel isOnlyModuleAllowedToUseJPanel;
    private MustUseJPanel mustUseJPanel;
    private NamingConventionExceptionJPanel namingConventionExceptionJPanel;
    private NamingConventionJPanel namingConventionJPanel;
    private SkipCallJPanel skipCallJPanel;
    private SuperClassInheritanceConventionJPanel superClassInheritanceConventionJPanel;
    private VisibilityConventionExceptionJPanel visibilityConventionExceptionJPanel;
    private VisibilityConventionJPanel visibilityConventionJPanel;

    public AbstractDetailsJPanel create(
	    AppliedRuleController appliedRuleController, String ruleTypeKey) {
	// Rules on the Contents of a module
	if (ruleTypeKey.equals(InterfaceConventionJPanel.ruleTypeKey)) {
	    interfaceConventionJPanel = new InterfaceConventionJPanel(
		    appliedRuleController);
	    return interfaceConventionJPanel;
	} else if (ruleTypeKey.equals(SuperClassInheritanceConventionJPanel.ruleTypeKey)) {
		superClassInheritanceConventionJPanel = new SuperClassInheritanceConventionJPanel(
		    appliedRuleController);
	    return superClassInheritanceConventionJPanel;
	} else if (ruleTypeKey.equals(VisibilityConventionJPanel.ruleTypeKey)) {
	    visibilityConventionJPanel = new VisibilityConventionJPanel(
		    appliedRuleController);
	    return visibilityConventionJPanel;
	} else if (ruleTypeKey
		.equals(VisibilityConventionExceptionJPanel.ruleTypeKey)) {
	    visibilityConventionExceptionJPanel = new VisibilityConventionExceptionJPanel(
		    appliedRuleController);
	    return visibilityConventionExceptionJPanel;
	} else if (ruleTypeKey.equals(NamingConventionJPanel.ruleTypeKey)) {
	    namingConventionJPanel = new NamingConventionJPanel(
		    appliedRuleController);
	    return namingConventionJPanel;
	} else if (ruleTypeKey
		.equals(NamingConventionExceptionJPanel.ruleTypeKey)) {
	    namingConventionExceptionJPanel = new NamingConventionExceptionJPanel(
		    appliedRuleController);
	    return namingConventionExceptionJPanel;
	    // Rules of the Legality of Dependency
	} else if (ruleTypeKey.equals(IsNotAllowedToUseJPanel.ruleTypeKey)) {
	    isNotAllowedToUseJPanel = new IsNotAllowedToUseJPanel(
		    appliedRuleController);
	    return isNotAllowedToUseJPanel;
	} else if (ruleTypeKey.equals(IsAllowedToUseJPanel.ruleTypeKey)) {
	    isAllowedToUseJPanel = new IsAllowedToUseJPanel(
		    appliedRuleController);
	    return isAllowedToUseJPanel;
	} else if (ruleTypeKey.equals(IsOnlyAllowedToUseJPanel.ruleTypeKey)) {
	    isOnlyAllowedToUseJPanel = new IsOnlyAllowedToUseJPanel(
		    appliedRuleController);
	    return isOnlyAllowedToUseJPanel;
	} else if (ruleTypeKey
		.equals(IsOnlyModuleAllowedToUseJPanel.ruleTypeKey)) {
	    isOnlyModuleAllowedToUseJPanel = new IsOnlyModuleAllowedToUseJPanel(
		    appliedRuleController);
	    return isOnlyModuleAllowedToUseJPanel;
	} else if (ruleTypeKey.equals(MustUseJPanel.ruleTypeKey)) {
	    mustUseJPanel = new MustUseJPanel(appliedRuleController);
	    return mustUseJPanel;
	} else if (ruleTypeKey.equals(SkipCallJPanel.ruleTypeKey)) {
	    skipCallJPanel = new SkipCallJPanel(appliedRuleController);
	    return skipCallJPanel;
	} else if (ruleTypeKey.equals(FacadeConventionRuleJPanel.ruleTypeKey)) {
	    facadeConventionJPanel = new FacadeConventionRuleJPanel(
		    appliedRuleController);
	    return facadeConventionJPanel;

	} else if (ruleTypeKey.equals(BackCallJPanel.ruleTypeKey)) {
	    backCallJPanel = new BackCallJPanel(appliedRuleController);
	    return backCallJPanel;
	    // Rules on the Dependency Limitation
	} else if (ruleTypeKey.equals(CyclesBetweenModulesJPanel.ruleTypeKey)) {
	    cyclesBetweenModules = new CyclesBetweenModulesJPanel(
		    appliedRuleController);
	    return cyclesBetweenModules;
	} else if (ruleTypeKey
		.equals(CyclesBetweenModulesExceptionJPanel.ruleTypeKey)) {
	    cyclesBetweenModulesExceptionJPanel = new CyclesBetweenModulesExceptionJPanel(
		    appliedRuleController);
	    return cyclesBetweenModulesExceptionJPanel;
	    // Not Known
	} else {
	    Logger.getLogger(FactoryDetails.class).error(
		    "No known AbstractDetailsJPanel for key: " + ruleTypeKey);
	    // throw new
	    // RuntimeException("No known AbstractDetailsJPanel for key: " +
	    // ruleTypeKey);
	    cyclesBetweenModules = new CyclesBetweenModulesJPanel(
		    appliedRuleController);
	    return cyclesBetweenModules;
	}
    }

    /**
     * 
     * @param ruleTypeKey
     * @return returns a the instance of the AbstractDetailsJPanel corrisonding
     *         with the ruleTypeKey. If the Implemented AbstractDetailsJPanel is
     *         null then it will return a new instance.
     */
    public AbstractDetailsJPanel get(
	    AppliedRuleController appliedRuleController, String ruleTypeKey) {
	// Rules on the Contents of a module
	if (ruleTypeKey.equals(InterfaceConventionJPanel.ruleTypeKey)) {
	    if (interfaceConventionJPanel == null) {
		interfaceConventionJPanel = new InterfaceConventionJPanel(
			appliedRuleController);
	    }
	    return interfaceConventionJPanel;
	} else if (ruleTypeKey.equals(SuperClassInheritanceConventionJPanel.ruleTypeKey)) {
	    if (superClassInheritanceConventionJPanel == null) {
	    	superClassInheritanceConventionJPanel = new SuperClassInheritanceConventionJPanel(
			appliedRuleController);
	    }
	    return superClassInheritanceConventionJPanel;
	} else if (ruleTypeKey.equals(VisibilityConventionJPanel.ruleTypeKey)) {
	    if (visibilityConventionJPanel == null) {
		visibilityConventionJPanel = new VisibilityConventionJPanel(
			appliedRuleController);
	    }
	    return visibilityConventionJPanel;
	} else if (ruleTypeKey
		.equals(VisibilityConventionExceptionJPanel.ruleTypeKey)) {
	    if (visibilityConventionExceptionJPanel == null) {
		visibilityConventionExceptionJPanel = new VisibilityConventionExceptionJPanel(
			appliedRuleController);
	    }
	    return visibilityConventionExceptionJPanel;
	} else if (ruleTypeKey.equals(NamingConventionJPanel.ruleTypeKey)) {
	    if (namingConventionJPanel == null) {
		namingConventionJPanel = new NamingConventionJPanel(
			appliedRuleController);
	    }
	    return namingConventionJPanel;
	} else if (ruleTypeKey
		.equals(NamingConventionExceptionJPanel.ruleTypeKey)) {
	    if (namingConventionExceptionJPanel == null) {
		namingConventionExceptionJPanel = new NamingConventionExceptionJPanel(
			appliedRuleController);
	    }
	    return namingConventionExceptionJPanel;
	    // Rules of the Legality of Dependency
	} else if (ruleTypeKey.equals(IsNotAllowedToUseJPanel.ruleTypeKey)) {
	    if (isNotAllowedToUseJPanel == null) {
		isNotAllowedToUseJPanel = new IsNotAllowedToUseJPanel(
			appliedRuleController);
	    }
	    return isNotAllowedToUseJPanel;
	} else if (ruleTypeKey.equals(IsAllowedToUseJPanel.ruleTypeKey)) {
	    if (isAllowedToUseJPanel == null) {
		isAllowedToUseJPanel = new IsAllowedToUseJPanel(
			appliedRuleController);
	    }
	    return isAllowedToUseJPanel;
	} else if (ruleTypeKey.equals(IsOnlyAllowedToUseJPanel.ruleTypeKey)) {
	    if (isOnlyAllowedToUseJPanel == null) {
		isOnlyAllowedToUseJPanel = new IsOnlyAllowedToUseJPanel(
			appliedRuleController);
	    }
	    return isOnlyAllowedToUseJPanel;
	} else if (ruleTypeKey
		.equals(IsOnlyModuleAllowedToUseJPanel.ruleTypeKey)) {
	    if (isOnlyModuleAllowedToUseJPanel == null) {
		isOnlyModuleAllowedToUseJPanel = new IsOnlyModuleAllowedToUseJPanel(
			appliedRuleController);
	    }
	    return isOnlyModuleAllowedToUseJPanel;
	} else if (ruleTypeKey.equals(MustUseJPanel.ruleTypeKey)) {
	    if (mustUseJPanel == null) {
		mustUseJPanel = new MustUseJPanel(appliedRuleController);
	    }
	    return mustUseJPanel;
	} else if (ruleTypeKey.equals(SkipCallJPanel.ruleTypeKey)) {
	    if (skipCallJPanel == null) {
		skipCallJPanel = new SkipCallJPanel(appliedRuleController);
	    }
	    return skipCallJPanel;
	} else if (ruleTypeKey.equals(BackCallJPanel.ruleTypeKey)) {
	    if (backCallJPanel == null) {
		backCallJPanel = new BackCallJPanel(appliedRuleController);
	    }
	    return backCallJPanel;
	    // Rules on the Dependency Limitation
	} else if (ruleTypeKey.equals(CyclesBetweenModulesJPanel.ruleTypeKey)) {
	    if (cyclesBetweenModules == null) {
		cyclesBetweenModules = new CyclesBetweenModulesJPanel(
			appliedRuleController);
	    }
	    return cyclesBetweenModules;
	} else if (ruleTypeKey
		.equals(CyclesBetweenModulesExceptionJPanel.ruleTypeKey)) {
	    if (cyclesBetweenModulesExceptionJPanel == null) {
		cyclesBetweenModulesExceptionJPanel = new CyclesBetweenModulesExceptionJPanel(
			appliedRuleController);
	    }
	    return cyclesBetweenModulesExceptionJPanel;
	    // Not Known
	} else {
	    Logger.getLogger(FactoryDetails.class).error(
		    "No known AbstractDetailsJPanel for key: " + ruleTypeKey);
	    throw new RuntimeException(
		    "No known AbstractDetailsJPanel for key: " + ruleTypeKey);
	}
    }
}
