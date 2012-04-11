package husacct.validate;

import com.itextpdf.text.DocumentException;
import husacct.define.DefineServiceStub;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.validate.abstraction.export.ExportReportFactory;
import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.assembler.AssemblerController;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.factory.RuletypesFactory;
import husacct.validate.presentation.BrowseViolations;
import husacct.validate.task.filter.LogicalPathsViolation;
import java.io.IOException;
import javax.swing.JInternalFrame;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class ValidateServiceImpl implements IValidateService {
	private ValidateServiceStub stub = new ValidateServiceStub();
	private CheckConformanceController checkConformance = new CheckConformanceController();

	public CategoryDTO[] getCategories(){
		RuletypesFactory ruletypefactory = new RuletypesFactory();
		return new AssemblerController().createCategoryDTO(ruletypefactory.getRuleTypes());
	}

	@Override
	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo) {
		return new LogicalPathsViolation().getViolations(logicalpathFrom, logicalpathTo, checkConformance);
	}

	@Override
	public String[] getExportExtentions() {
		return stub.getExportExtentions();
	}

	@Override
	public void checkConformance() {
		RuleDTO[] appliedRules = new RuleDTO[]{new DefineServiceStub().getDefinedRules()[0]};
		System.out.println("check");
		checkConformance.CheckConformance(appliedRules);
	}

	@Override
	//Export report
	public void exportViolations(String name, String fileType, String path) {
		System.out.println("export");
		try {
			new ExportReportFactory().exportReport(fileType, checkConformance.getViolations(), name+".pdf", path);
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnknownStorageTypeException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public JInternalFrame getBrowseViolationsGUI(){
		return new BrowseViolations(checkConformance.getViolations());
	}

	public static void main(String[] args){
		ValidateServiceImpl serviceImpl = new ValidateServiceImpl();
		serviceImpl.checkConformance();
		serviceImpl.exportViolations("elaborationViolations.pdf", "pdf", "C:\\reports");
	}

}