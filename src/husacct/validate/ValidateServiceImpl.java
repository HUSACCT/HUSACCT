package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.savechain.ISaveable;
import husacct.define.DefineServiceStub;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.presentation.BrowseViolations;
import husacct.validate.presentation.ConfigurationUI;
import husacct.validate.task.ReportServiceImpl;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.report.UnknownStorageTypeException;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JInternalFrame;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Element;

import com.itextpdf.text.DocumentException;

public class ValidateServiceImpl implements IValidateService, ISaveable {
	private boolean validationExecuted;

	private ConfigurationServiceImpl configuration;
	private DomainServiceImpl domain;
	private ReportServiceImpl report;
	private TaskServiceImpl task;
	//private AbstractionServiceImpl abstraction;

	public ValidateServiceImpl(){
		this.configuration = new ConfigurationServiceImpl();
		this.domain = new DomainServiceImpl(configuration);
		this.report = new ReportServiceImpl(configuration);
		this.task = new TaskServiceImpl(configuration, domain);
		//this.abstraction = new AbstractionServiceImpl();
		this.validationExecuted = false;
	}


	@Override
	public CategoryDTO[] getCategories(){
		return domain.getCategories();
	}
	
	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		return task.getViolationsByLogicalPath(logicalpathFrom, logicalpathTo);
	}
	
	@Override
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo) {
		return task.getViolationsByPhysicalPath(physicalpathFrom, physicalpathTo);
	}


	@Override
	public String[] getExportExtentions() {
		return task.getExportExtentions();
	}

	@Override
	public void checkConformance() {		
		RuleDTO[] appliedRules = new DefineServiceStub().getDefinedRules();
		domain.checkConformance(appliedRules);
		this.validationExecuted = true;
	}

	@Override
	//Export report
	public void exportViolations(String name, String fileType, String path) throws UnknownStorageTypeException, IOException, URISyntaxException, DocumentException  {
		report.createReport(fileType, name, path);
	}

	@Override
	public JInternalFrame getBrowseViolationsGUI(){
		return new BrowseViolations(task);
	}

	@Override
	public JInternalFrame getConfigurationGUI(){
		return new ConfigurationUI(task);
	}

	@Override
	public Element getWorkspaceData() {
		return task.exportValidationWorkspace();
	}


	@Override
	public void loadWorkspaceData(Element workspaceData) {
		try {
			task.importValidationWorkspace(workspaceData);
			this.validationExecuted = true;
		} catch (DatatypeConfigurationException e) {
			Logger.getLogger(ValidateServiceImpl.class).log(Level.ERROR, "Error exporting the workspace", e);
		}
	}

	@Override
	public boolean isValidated() {
		return validationExecuted;
	}
	
	public ConfigurationServiceImpl getConfiguration() {
		return configuration;
	}
	
	public void Validate(RuleDTO[] appliedRules){
		domain.checkConformance(appliedRules);
	}
	
	public static void main(String[] args){
		ValidateServiceImpl serviceImpl = new ValidateServiceImpl();
		serviceImpl.checkConformance();

	}
}