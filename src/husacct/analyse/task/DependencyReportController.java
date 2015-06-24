package husacct.analyse.task;

import husacct.analyse.abstraction.export.AbstractReportFileExporter;
import husacct.analyse.abstraction.export.NoDataException;
import husacct.analyse.abstraction.export.excel.ExcelExporter;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.DependencyDTO;

import org.apache.log4j.Logger;

public class DependencyReportController {

    private Logger husacctLogger = Logger.getLogger(DependencyReportController.class);
    private IModelQueryService queryService;
    private AbstractReportFileExporter fileExporter;
    private DependencyDTO[] exportData;

    public DependencyReportController() {
    }
    
    public DependencyReportController(IModelQueryService queryService){
    	this.queryService = queryService;
    }

    public void createDependencyReport(String path) {
        exportData = queryService.getAllDependencies();
        fileExporter = new ExcelExporter(exportData, queryService);
        try {
            fileExporter.writeToFile(path);
        } catch (NoDataException noDataException) {
            husacctLogger.info("Did not write dependencies to file, because no dependency data is available");
        }
    }
}
