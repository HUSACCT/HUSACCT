package husacct.analyse.abstraction.export;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.dto.DependencyDTO;

public abstract class AbstractReportFileExporter {

    protected DependencyDTO[] data;

    protected String translate(String key) {
        return ServiceProvider.getInstance().getLocaleService().getTranslatedString(key);
    }

    public AbstractReportFileExporter(DependencyDTO[] data) {
        this.data = data;
    }

    public void writeToFile(String path) throws NoDataException {
        if (data == null || data.length == 0) {
            throw new NoDataException();
        } else {
            write(path);
        }
    }

    protected abstract void write(String path);
}