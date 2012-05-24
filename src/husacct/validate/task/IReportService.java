package husacct.validate.task;

import husacct.validate.domain.validation.ViolationHistory;

public interface IReportService {
	
	public String[] getExportExtentions();
	public void createReport(String fileType, String name, String path);
	public void createReport(String fileType, String name, String path,
			ViolationHistory violationHistory);

}
