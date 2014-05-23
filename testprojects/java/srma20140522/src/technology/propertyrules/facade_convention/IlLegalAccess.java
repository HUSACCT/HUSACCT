package technology.propertyrules.facade_convention;

import technology.propertyrules.component.IllegalVisibility1;

public class IlLegalAccess {
	private IllegalVisibility1 legalInformation;
	
	public IlLegalAccess(){
		this.legalInformation = new IllegalVisibility1();
	}
	
	public String getTermsOfService(){
		return legalInformation.getTermsOfService();
	} 

	public String getPrivacyPolicy(){
		return legalInformation.getPrivacyPolicy();
	}
}