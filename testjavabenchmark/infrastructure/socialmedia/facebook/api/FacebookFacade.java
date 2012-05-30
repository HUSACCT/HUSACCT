package infrastructure.socialmedia.facebook.api;

import java.io.IOException;

public class FacebookFacade {
	private FacebookGraph facebookGraph;

	public FacebookFacade(){
		facebookGraph = new FacebookGraph();
	}

	public boolean getGraphId(String url){
		try {
			return facebookGraph.isValidFacebookGraphUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return false;
	}
}