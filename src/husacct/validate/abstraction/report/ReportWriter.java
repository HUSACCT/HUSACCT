package husacct.validate.abstraction.report;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

public abstract class ReportWriter {

	protected Report report;
	protected String path;
	protected String fileName;

	public ReportWriter(Report report, String path, String fileName) {
		this.report = report;
		this.path = path;
		this.fileName = fileName;
	}

	public abstract void createReport() throws IOException, DocumentException, JAXBException, ParserConfigurationException, TransformerException, DOMException, SAXException, UnknownStorageTypeException;

	public String convertIsIndirectBooleanToString(boolean isIndirect) {
		if(isIndirect) {
			return "direct";
		} else {
			return "indirect";
		}
	}
	public String getCurrentDate(){
		return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}
	
	public void checkDirsExist() {
		File file = new File(path);
		file.mkdirs();
	}
	
	public String getFileName() {
		String s = "";
		if(path.endsWith(""+File.separatorChar)) {
			s = path + fileName;
		} else {
			s = path + File.separatorChar + fileName;
		}
		return s;
	}
}
