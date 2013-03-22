package husaccttest.control;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.control.IControlService;

import org.junit.Before;
import org.junit.Test;

public class ObservableServiceTest {

    private IControlService service;

    @Before
    public void setup() {
        service = ServiceProvider.getInstance().getControlService();
    }

    @Test
    public void testConcurrentModification() {
        service.addServiceListener(new IServiceListener() {
            @Override
            public void update() {

                // Adding another listener while being notified should not raise a ConcurrentModificatinException
                service.addServiceListener(new IServiceListener() {
                    @Override
                    public void update() {
                    }
                });
            }
        });
        service.notifyServiceListeners();
    }
}
