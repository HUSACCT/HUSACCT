package husacct.validate;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.savechain.ISaveable;
import husacct.define.IDefineService;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.presentation.GuiController;
import husacct.validate.task.ReportServiceImpl;
import husacct.validate.task.TaskServiceImpl;

import javax.swing.JInternalFrame;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ValidateServiceImpl implements IValidateService, ISaveable {		
	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	private Logger logger = Logger.getLogger(ValidateServiceImpl.class);
	
	private final GuiController gui;
	private final ConfigurationServiceImpl configuration;
	private final DomainServiceImpl domain;
	private final ReportServiceImpl report;
	private final TaskServiceImpl task;

	private boolean validationExecuted;

	public ValidateServiceImpl(){
		this.configuration = new ConfigurationServiceImpl();
		this.domain = new DomainServiceImpl(configuration);
		this.report = new ReportServiceImpl(configuration);
		this.task = new TaskServiceImpl(configuration, domain);
		this.gui = new GuiController(task);
		this.validationExecuted = false;
	}

	@Override
	public CategoryDTO[] getCategories(){
		return domain.getCategories();
	}
	
	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {		
		if(!validationExecuted){
			logger.debug("warning, method: getViolationsByLogicalPath executed but no validation is executed");
		}		
		return task.getViolationsByLogicalPath(logicalpathFrom, logicalpathTo);
	}
	
	@Override
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo) {
		if(!validationExecuted){
			logger.debug("warning, method: getViolationsByPhysicalPath executed but no validation is executed");
		}	
		return task.getViolationsByPhysicalPath(physicalpathFrom, physicalpathTo);
	}

	@Override
	public String[] getExportExtentions() {
		return task.getExportExtentions();
	}

	@Override
	public void checkConformance() {		
		RuleDTO[] appliedRules = defineService.getDefinedRules();
		domain.checkConformance(appliedRules);
		this.validationExecuted = true;
	}

	@Override
	//Export report
	public void exportViolations(String name, String fileType, String path) {
		report.createReport(fileType, name, path);
	}

	@Override
	public JInternalFrame getBrowseViolationsGUI(){
		return gui.getBrowseViolationsGUI();
	}

	@Override
	public JInternalFrame getConfigurationGUI(){
		return gui.getConfigurationGUI();
	}

	@Override
	public Element getWorkspaceData() {
		return task.exportValidationWorkspace();
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		try {
			task.importValidationWorkspace(workspaceData);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error exporting the workspace: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean isValidated() {
		return validationExecuted;
	}
	
	//This method is only used for testing with the Testsuite
	public ConfigurationServiceImpl getConfiguration() {
		return configuration;
	}
	
	//This method is only used for testing with the Testsuite
	public void Validate(RuleDTO[] appliedRules){
		domain.checkConformance(appliedRules);
	}
}