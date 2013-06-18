package domain.indirect.intermediate;

import domain.indirect.indirectto.ServiceOne;
import domain.indirect.indirectto.POI;
import domain.indirect.intermediate.ServiceTwo;

public class BackgroundService {
	public static ServiceOne serviceOneStaticAttribute;
	public  ServiceOne serviceOne;
	public  ServiceTwo serviceTwo;

	public static ServiceOne getServiceOneviaStaticAttribute(){
		return serviceOneStaticAttribute;
	}

	public ServiceOne getServiceOne() {
		return serviceOne;
	}
	public ServiceTwo getServiceTwo() {
		return serviceTwo;
	}
	
	public String getServiceOneNameByArgument(ServiceOne so){
		return so.getName();
	}

	public String getPOINameByArgument(POI poi){
		return poi.getName();
	}

}