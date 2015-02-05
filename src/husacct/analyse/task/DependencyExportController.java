package husacct.analyse.task;

import husacct.analyse.abstraction.export.AbstractFileExporter;
import husacct.analyse.abstraction.export.NoDataException;
import husacct.analyse.abstraction.export.excel.ExcelExporter;
import husacct.analyse.domain.IAnalyseDomainService;
import husacct.common.dto.DependencyDTO;
import org.apache.log4j.Logger;

public class DependencyExportController {

    private Logger husacctLogger = Logger.getLogger(DependencyExportController.class);
    private IAnalyseDomainService analysedDomain;
    private AbstractFileExporter fileExporter;
    private DependencyDTO[] exportData;

    public DependencyExportController() {
    }
    
    public DependencyExportController(IAnalyseDomainService analyseDomainService){
    	this.analysedDomain = analyseDomainService;
    }

    public void export(String path) {
        exportData = analysedDomain.mapDependencies();
        fileExporter = new ExcelExporter(exportData, analysedDomain);
        try {
            fileExporter.writeToFile(path);
        } catch (NoDataException noDataException) {
            husacctLogger.info("Did not write dependencies to file, because no dependency data is available");
        }
    }
}
