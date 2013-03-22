package husacct.common.services;

public interface IObservableService {

    public void addServiceListener(IServiceListener listener);

    public void notifyServiceListeners();
}
