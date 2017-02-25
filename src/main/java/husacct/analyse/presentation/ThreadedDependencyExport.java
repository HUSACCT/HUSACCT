package husacct.analyse.presentation;

import org.apache.log4j.Logger;

class ThreadedDependencyExport implements Runnable {

    private Logger husacctLogger = Logger.getLogger(ThreadedDependencyExport.class);
    private AnalyseUIController dataControl;
    private String path;

    public ThreadedDependencyExport(AnalyseUIController control, String filePath) {
        this.dataControl = control;
        this.path = filePath;
    }

    public void run() {
        try {
            Thread.sleep(20);
            dataControl.exportDependencies(path);
        } catch (InterruptedException exception) {
            husacctLogger.debug("Analyse export dependencies interrupted");
        }
    }
}
