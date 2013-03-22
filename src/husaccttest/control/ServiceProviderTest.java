package husaccttest.control;

import static org.junit.Assert.assertSame;
import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husacct.graphics.IGraphicsService;
import husacct.validate.IValidateService;

import org.junit.Before;
import org.junit.Test;

public class ServiceProviderTest {

    IAnalyseService analyseService;
    IGraphicsService architectureService;
    IDefineService defineService;
    IValidateService validateService;

    @Before
    public void prepareServices() {
        ServiceProvider provider = ServiceProvider.getInstance();
        this.analyseService = provider.getAnalyseService();
        this.architectureService = provider.getGraphicsService();
        this.defineService = provider.getDefineService();
        this.validateService = provider.getValidateService();
    }

    @Test
    public void testServiceProvider() {
        ServiceProvider provider1 = ServiceProvider.getInstance();
        ServiceProvider provider2 = ServiceProvider.getInstance();
        assertSame(provider1, provider2);
    }

    @Test
    public void testServiceProviderGetAnalyseService() {
        ServiceProvider provider1 = ServiceProvider.getInstance();
        ServiceProvider provider2 = ServiceProvider.getInstance();
        assertSame(provider1.getAnalyseService(), provider2.getAnalyseService());
    }

    @Test
    public void testServiceProviderGetGraphicsService() {
        ServiceProvider provider1 = ServiceProvider.getInstance();
        ServiceProvider provider2 = ServiceProvider.getInstance();
        assertSame(provider1.getGraphicsService(), provider2.getGraphicsService());
    }

    @Test
    public void testServiceProviderGetDefineService() {
        ServiceProvider provider1 = ServiceProvider.getInstance();
        ServiceProvider provider2 = ServiceProvider.getInstance();
        assertSame(provider1.getDefineService(), provider2.getDefineService());
    }

    @Test
    public void testServiceProviderGetValidateService() {
        ServiceProvider provider1 = ServiceProvider.getInstance();
        ServiceProvider provider2 = ServiceProvider.getInstance();
        assertSame(provider1.getValidateService(), provider2.getValidateService());
    }
}
