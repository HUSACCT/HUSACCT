package husacct.common.credits;

import husacct.common.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class creditsFetcher {

	private Logger logger = Logger.getLogger(creditsFetcher.class);
	
	public List<String> fetchDeveloperNames() {
		
		List<String> developers = new ArrayList<String>();
		try {
			InputStream stream = Resource.getStream((Resource.CREDITS_PATH) +"credits_developers.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line;
			while((line = br.readLine()) != null) {
				if(line.charAt(0) != '@') {					
					developers.add(line);
				}
			}
			return developers;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return developers;
	}
}
