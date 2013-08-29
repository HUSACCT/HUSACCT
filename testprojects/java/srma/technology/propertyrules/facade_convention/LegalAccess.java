package technology.propertyrules.facade_convention;

import technology.propertyrules.component.ComponentInterface;

public class LegalAccess {
	private ComponentInterface twitter;
	
	public LegalAccess(){
		twitter = new ComponentInterface();
	}
	
	public String getTermsOfService(){
		return twitter.getTermsOfService();
	}
	
	public String getPrivacyPolicy(){
		return twitter.getTermsOfService();
	}
}