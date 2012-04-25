package husacct.validate;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JInternalFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.MessageDTO;
import husacct.common.dto.ViolationDTO;
import husacct.validate.task.report.UnknownStorageTypeException;

public interface IValidateService
{
	public String buildDefinedRuleMessage(MessageDTO message);
	public CategoryDTO[] getCategories();
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo);
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo);
	public String[] getExportExtentions();
	public void checkConformance();
	public void exportViolations(String name, String fileType, String path) throws DOMException, UnknownStorageTypeException, ParserConfigurationException, SAXException, IOException, URISyntaxException, DocumentException, TransformerException;
	public boolean isValidated();
	public JInternalFrame getBrowseViolationsGUI();
	public JInternalFrame getConfigurationGUI();
}