package husacct.validate;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;
import husacct.define.IDefineService;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.presentation.GuiController;
import husacct.validate.task.ReportServiceImpl;
import husacct.validate.task.TaskServiceImpl;

import java.io.File;
import java.util.Calendar;

import javax.swing.JInternalFrame;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public final class ValidateServiceImpl extends ObservableService implements
		IValidateService, ISaveable {

	private final IDefineService defineService = ServiceProvider.getInstance()
			.getDefineService();
	private Logger logger = Logger.getLogger(ValidateServiceImpl.class);
	private final GuiController gui;
	private final ConfigurationServiceImpl configuration;
	private final DomainServiceImpl domain;
	private final ReportServiceImpl report;
	private final TaskServiceImpl task;
	private boolean validationExecuted;

	public ValidateServiceImpl() {
		this.configuration = new ConfigurationServiceImpl();
		this.domain = new DomainServiceImpl(configuration);
		this.task = new TaskServiceImpl(configuration, domain);
		this.report = new ReportServiceImpl(task);
		this.gui = new GuiController(task, configuration);
		this.validationExecuted = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryDTO[] getCategories() {
		return domain.getCategories();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom,
			String logicalpathTo) {
		if (!validationExecuted) {
			logger.debug("warning, method: getViolationsByLogicalPath executed but no validation is executed");
		}
		return task.getViolationsByLogicalPath(logicalpathFrom, logicalpathTo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom,
			String physicalpathTo) {
		if (!validationExecuted) {
			logger.debug("warning, method: getViolationsByPhysicalPath executed but no validation is executed");
		}
		return task.getViolationsByPhysicalPath(physicalpathFrom,
				physicalpathTo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getExportExtentions() {
		return report.getExportExtentions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkConformance() {
		RuleDTO[] appliedRules = defineService.getDefinedRules();
		domain.checkConformance(appliedRules);
		this.validationExecuted = true;
		notifyServiceListeners();
		gui.violationChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JInternalFrame getBrowseViolationsGUI() {
		return gui.getBrowseViolationsGUI();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JInternalFrame getConfigurationGUI() {
		return gui.getConfigurationGUI();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Element getWorkspaceData() {
		return task.exportValidationWorkspace();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadWorkspaceData(Element workspaceData) {
		try {
			task.importValidationWorkspace(workspaceData);
		} catch (DatatypeConfigurationException e) {
			logger.error("Error exporting the workspace: " + e.getMessage(), e);
		}
		notifyServiceListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidated() {
		return validationExecuted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Calendar[] getViolationHistoryDates() {
		return task.getViolationHistoryDates();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportViolations(File file, String fileType, Calendar date) {
		report.createReport(file, fileType, date);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportViolations(File file, String fileType) {
		report.createReport(file, fileType);
	}

	/**
	 * This method is only used for testing with the Testsuite
	 * 
	 * @return a ConfigurationServiceImpl object
	 */
	public ConfigurationServiceImpl getConfiguration() {
		return configuration;
	}

	/**
	 * This method is only used for testing with the Testsuite It checks an
	 * array of applied rules for conformance.
	 * 
	 * @param appliedRules an array of applied rules.
	 */
	public void Validate(RuleDTO[] appliedRules) {
		domain.checkConformance(appliedRules);
	}

	@Override
	public RuleTypeDTO[] getDefaultRuleTypesOfModule(String type) {
		return domain.getDefaultRuleTypeOfModule(type);
	}

	@Override
	public RuleTypeDTO[] getAllowedRuleTypesOfModule(String type) {
		return domain.getAllowedRuleTypeOfModule(type);
	}
}