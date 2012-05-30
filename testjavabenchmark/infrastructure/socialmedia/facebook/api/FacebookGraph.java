package infrastructure.socialmedia.facebook.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
//Functional requirement 2.3
//Test case 4: Only the classes in package infrastructure.socialmedia.facebook.api are allowed to use the JSON.jar library file
//Result: TRUE

//Functional requirement 4.5
//Test case 202: Class infrastructure.socialmedia.facebook.API.FacebookGraph is only allowed to get data from the FacebookGraph REST API.
//Result: TRUE
class FacebookGraph {
	public boolean isValidFacebookGraphUrl(String url) throws IOException {
		//Valid URL: https://graph.facebook.com/19292868552
		if(url.contains("https://graph.facebook.com/")){			
			try{
				//JSONObject is from the JSON.jar library
				JSONObject json = readJsonFromUrl(url);
				if(json.toString().equals("false")){
					return false;
				}
				else{
					System.out.println(json.get("id"));
					return true;
				}	
			}
			//JSONException is from the JSON.jar library
			catch(JSONException e){

			}
		}
		else{
			return false;
		}
		return false;
	}

	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			//JSONObject is from the JSON.jar library
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}