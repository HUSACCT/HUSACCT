package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.MessageDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.savechain.ISaveable;
import husacct.define.DefineServiceStub;
import husacct.validate.abstraction.AbstractionServiceImpl;
import husacct.validate.abstraction.extensiontypes.ExtensionTypes;
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

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
	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo) {
		return task.getViolations(logicalpathFrom, logicalpathTo);
	}

	@Override
	public String[] getExportExtentions() {
		return new ExtensionTypes().getExtensionTypes();
	}

	@Override
	public void checkConformance() {		
		RuleDTO[] appliedRules = new DefineServiceStub().getDefinedRules();
		domain.checkConformance(appliedRules);
		this.validationExecuted = true;
	}

	@Override
	//Export report
	public void exportViolations(String name, String fileType, String path) throws DOMException, UnknownStorageTypeException, ParserConfigurationException, SAXException, IOException, URISyntaxException, DocumentException, TransformerException {
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

	public static void main(String[] args){
		ValidateServiceImpl serviceImpl = new ValidateServiceImpl();
		serviceImpl.checkConformance();

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
			this.validationExecuted = false;
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
}