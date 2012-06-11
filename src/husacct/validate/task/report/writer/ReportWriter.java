package husacct.validate.task.report.writer;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

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

	protected String getDependencyKindValue(String violationtypeKey,
			boolean indirect) {
		if(!violationtypeKey.isEmpty()) {
			String value = ServiceProvider.getInstance().getControlService().getTranslatedString(violationtypeKey);
			value += ", ";
			if(!violationtypeKey.equals("VisibilityConvention")) {
				if(indirect) {
					value += "indirect";
				} else {
					value += "direct";
				}
			}
			return value;
		}
		return "";
	}


	public String getCurrentDate(){
		return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}

	public void checkDirsExist() {
		File file = new File(path);
		file.mkdirs();
	}

	public String getFileName() {
		return path + "\\" + fileName;
	}
}
