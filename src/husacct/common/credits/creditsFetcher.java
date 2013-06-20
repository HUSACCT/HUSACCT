package husacct.common.credits;

import husacct.common.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class creditsFetcher {

	private Logger logger = Logger.getLogger(creditsFetcher.class);
	
	public List<String> fetchStudentNames() {
		
		List<String> students = new ArrayList<String>();
		try {
			InputStream stream = Resource.getStream((Resource.CREDITS_PATH) +"credits_students.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			//String CreditsPath = URLDecoder.decode((Resource.get(Resource.CREDITS_PATH) +"credits_students.txt").replaceAll("file:/", ""), "utf-8");
			//File f = new File(CreditsPath);
			//BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while((line = br.readLine()) != null) {
				if(line.charAt(0) != '@') {					
					students.add(line);
				}
			}
			return students;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return students;
	}
	public List<String> fetchTeacherNames() {
		
		List<String> teacher = new ArrayList<String>();
		try {
			//String CreditsPath = URLDecoder.decode((Resource.get(Resource.CREDITS_PATH) +"credits_teachers.txt").replaceAll("file:/", ""), "utf-8");
			//File f = new File(CreditsPath);
			//BufferedReader br = new BufferedReader(new FileReader(f));
			InputStream stream = Resource.getStream((Resource.CREDITS_PATH) +"credits_teachers.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line;
			while((line = br.readLine()) != null) {
				if(line.charAt(0) != '@') {					
					teacher.add(line);
				}
			}
			return teacher;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return teacher;
	}
}
