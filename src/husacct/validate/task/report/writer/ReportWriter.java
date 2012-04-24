package husacct.validate.task.report.writer;

import husacct.validate.abstraction.extensiontypes.ExtensionTypes.ExtensionType;
import husacct.validate.domain.validation.report.Report;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.itextpdf.text.DocumentException;

public abstract class ReportWriter {

	protected Report report;
	protected String path;
	protected String fileName;
	protected ExtensionType extensionType;

	public ReportWriter(Report report, String path, String fileName, ExtensionType extensionType) {
		this.report = report;
		this.path = path;
		this.fileName = fileName;
		this.extensionType = extensionType;
	}

	public abstract void createReport() throws IOException, URISyntaxException, DocumentException ;

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
		s += "." + extensionType.getExtension();
		return s;
	}
}
