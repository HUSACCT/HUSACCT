package infrastructure.socialmedia.linkedin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.linkedin.Company;
//Functional requirement 4.5
//Test case 204: tThe following classes have a circular dependency:
//					* domain.linkedin.Company has a dependency with domain.linkedin.Person
//					* domain.linkedin.Person has a dependency with infrastructure.socialmedia.linkedin.JobDAO
//					* infrastructure.socialmedia.linkedin.JobDAO has a dependency with domain.linkedin.Company
//Result: TRUE
public class JobDAO {

	public List<String> getInterestedJobs() {
		List<String> returnList = new ArrayList<String>();		
		returnList.add(new Company().getName());
		return Collections.emptyList();
	}
}