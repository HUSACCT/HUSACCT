package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.define.DefineServiceImpl;
import husacct.define.DefineServiceStub;
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

public class ValidateServiceImpl implements IValidateService {
	private ConfigurationServiceImpl configuration;
	private DomainServiceImpl domain;
	private ReportServiceImpl report;
	private TaskServiceImpl task;

	public ValidateServiceImpl(){
		this.configuration = new ConfigurationServiceImpl();
		this.domain = new DomainServiceImpl(configuration);
		this.report = new ReportServiceImpl(configuration);
		this.task = new TaskServiceImpl(configuration, domain);
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

}