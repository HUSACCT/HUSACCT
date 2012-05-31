package husacct.analyse.task;

import java.util.HashMap;
import org.apache.log4j.Logger;
import husacct.analyse.abstraction.export.AbstractFileExporter;
import husacct.analyse.abstraction.export.NoDataException;
import husacct.analyse.abstraction.export.excel.ExcelExporter;
import husacct.analyse.domain.AnalyseDomainServiceImpl;
import husacct.analyse.domain.IAnalyseDomainService;
import husacct.common.dto.DependencyDTO;

public class DependencyExportController {
	
	private Logger husacctLogger = Logger.getLogger(DependencyExportController.class);
	private IAnalyseDomainService analysedDomain;
	private AbstractFileExporter fileExporter;
	
	public DependencyExportController(){
		this.analysedDomain = new AnalyseDomainServiceImpl();
		HashMap<String, DependencyDTO> exportData = analysedDomain.mapDependencies();
		fileExporter = new ExcelExporter(exportData);
	}
	
	public void export(String path){
		try{
			fileExporter.writeToFile(path);
		}catch(NoDataException noDataException){
			husacctLogger.info("Did not write dependencies to file, because no dependency data is available");
		}
	}
}
