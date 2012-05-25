package husacct.validate;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;
import husacct.define.IDefineService;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Violation;
import husacct.validate.presentation.GuiController;
import husacct.validate.task.ReportServiceImpl;
import husacct.validate.task.TaskServiceImpl;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public final class ValidateServiceImpl extends ObservableService implements IValidateService, ISaveable {		
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
		this.task = new TaskServiceImpl(configuration, domain);
		this.report = new ReportServiceImpl(task);
		this.gui = new GuiController(task, configuration);
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
		return report.getExportExtentions();
	}

	@Override
	public void checkConformance() {		
		RuleDTO[] appliedRules = defineService.getDefinedRules();
		domain.checkConformance(appliedRules);		
		this.validationExecuted = true;
		notifyServiceListeners();
		gui.violationChanged();
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
		notifyServiceListeners();
	}

	@Override
	public boolean isValidated() {
		return validationExecuted;
	}

	@Override
	public List<Violation> getHistoryViolationsByDate(Calendar date) {
		return task.getHistoryViolations(date);
	}

	@Override
	public Calendar[] getViolationHistoryDates() {
		return task.getViolationHistoryDates();
	}

	@Override
	public void createHistoryPoint(String description) {
		task.createHistoryPoint(description);	
		notifyServiceListeners();
	}

	@Override
	public void exportViolations(File file, String fileType, Calendar date) {
		report.createReport(file, fileType, date);
	}

	@Override
	public void exportViolations(File file, String fileType) {
		report.createReport(file, fileType);
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