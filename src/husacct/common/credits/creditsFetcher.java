package husacct.common.credits;

import husacct.common.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class creditsFetcher {

	public List<String> fetchStudentNames() {
		
		List<String> students = new ArrayList<String>();
		try {
			String CreditsPath = (Resource.get(Resource.CREDITS_PATH) +"credits_students.txt").replaceAll("file:/", "");
			File f = new File(CreditsPath);
			BufferedReader br = new BufferedReader(new FileReader(f));
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
			String CreditsPath = (Resource.get(Resource.CREDITS_PATH) +"credits_teachers.txt").replaceAll("file:/", "");
			File f = new File(CreditsPath);
			BufferedReader br = new BufferedReader(new FileReader(f));
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
