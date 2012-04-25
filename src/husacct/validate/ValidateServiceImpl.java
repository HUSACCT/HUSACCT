package husacct.validate;

import java.io.IOException;
import java.net.URISyntaxException;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.MessageDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.savechain.ISaveable;
import husacct.define.DefineServiceStub;
import husacct.validate.abstraction.AbstractionServiceImpl;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.presentation.BrowseViolations;
import husacct.validate.presentation.ConfigurationUI;
import husacct.validate.task.ReportServiceImpl;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.report.UnknownStorageTypeException;

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
	private AbstractionServiceImpl abstraction;

	public ValidateServiceImpl(){
		this.configuration = new ConfigurationServiceImpl();
		this.domain = new DomainServiceImpl(configuration);
		this.report = new ReportServiceImpl(configuration);
		this.task = new TaskServiceImpl(configuration, domain);
		this.abstraction = new AbstractionServiceImpl(configuration);
		this.validationExecuted = false;
	}


	@Override
	public CategoryDTO[] getCategories(){
		return domain.getCategories();
	}
	
	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
<<<<<<< HEAD
		return task.getViolations(logicalpathFrom, logicalpathTo);
=======
		return task.getViolationsByLogicalPath(logicalpathFrom, logicalpathTo);
	}
	
	@Override
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo) {
		return task.getViolationsByPhysicalPath(physicalpathFrom, physicalpathTo);
>>>>>>> 93be94728f800ccaf7972e05e10db14036d599c6
	}
	
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalPathFrom, String physicalPathTo) {
		return task.getViolationsByPhysicalPath(physicalPathFrom, physicalPathTo);
	}

	@Override
	public String[] getExportExtentions() {
		return abstraction.getExportExtentions();
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
		return abstraction.exportValidationWorkspace();
	}


	@Override
	public void loadWorkspaceData(Element workspaceData) {
		try {
			abstraction.importValidationWorkspace(workspaceData);
			this.validationExecuted = true;
		} catch (DatatypeConfigurationException e) {
			Logger.getLogger(ValidateServiceImpl.class).log(Level.ERROR, "Error exporting the workspace", e);
		}
	}

	@Override
	public boolean isValidated() {
		return validationExecuted;
	}

	@Override
	public String buildDefinedRuleMessage(MessageDTO message) {
		return domain.buildMessage(message);
	}
	
	public ConfigurationServiceImpl getConfiguration() {
		return configuration;
	}
	
	public static void main(String[] args){
		ValidateServiceImpl serviceImpl = new ValidateServiceImpl();
		serviceImpl.checkConformance();

	}
}