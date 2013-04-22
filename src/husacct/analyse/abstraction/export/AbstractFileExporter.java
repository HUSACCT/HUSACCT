package husacct.analyse.abstraction.export;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import java.util.HashMap;

public abstract class AbstractFileExporter {

    protected HashMap<String, DependencyDTO> data;

    protected String translate(String key) {
        return ServiceProvider.getInstance().getLocaleService().getTranslatedString(key);
    }

    public AbstractFileExporter(HashMap<String, DependencyDTO> data) {
        this.data = data;
    }

    public void writeToFile(String path) throws NoDataException {
        if (data.isEmpty()) {
            throw new NoDataException();
        } else {
            write(path);
        }
    }

    protected abstract void write(String path);
}