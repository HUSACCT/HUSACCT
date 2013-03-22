package husaccttest.analyse.java.visibillities;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husaccttest.analyse.TestCaseExtended;
import husaccttest.analyse.TestProjectFinder;

public class VisibillityTestExtended extends TestCaseExtended {

    protected final String PUBLIC = "public";
    protected final String PRIVATE = "private";
    protected final String DEFAULT = "default";
    protected IAnalyseService service;
    private static boolean isAnalysed = false;

    @Override
    public void setUp() {

        service = new AnalyseServiceImpl();
        String path = TestProjectFinder.lookupProject("java", "visibillity");
        String[] paths = new String[]{path};
        String language = "Java";

        try {
            if (!isAnalysed) {
                service.analyseApplication(paths, language);
                isAnalysed = true;
            }
        } catch (Exception e) {
            System.out.println("We're sorry. You need to have a java project 'benchmark_application' with inside the benchmark_application. Or you have the wrong version of the benchmark_application.");
            System.exit(0);
        }
    }

    @Override
    public void tearDown() {
    }
}
