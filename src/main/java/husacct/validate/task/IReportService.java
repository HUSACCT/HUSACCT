package husacct.validate.task;

import java.io.File;
import java.util.Calendar;

public interface IReportService {

	public String[] getExportExtentions();

	public void createReport(File file, String fileType, Calendar date);

	public void createReport(File file, String fileType);
}