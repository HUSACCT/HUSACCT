package husacct.common.credits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class creditsFetcher {

	public List<String> fetchStudentNames() {
		
		List<String> students = new ArrayList<String>();
		try {
			File f = new File(new File(".").getCanonicalPath() + "\\src\\husacct\\common\\resources\\credits\\credits_students.txt");
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
			File f = new File(new File(".").getCanonicalPath() + "\\src\\husacct\\common\\resources\\credits\\credits_teachers.txt");
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
