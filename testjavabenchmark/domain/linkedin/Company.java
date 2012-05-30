package domain.linkedin;

import java.util.List;
//Functional requirement 4.5
//Test case 204: tThe following classes have a circular dependency:
//					* domain.linkedin.Company has a dependency with domain.linkedin.Person
//					* domain.linkedin.Person has a dependency with infrastructure.socialmedia.linkedin.JobDAO
//					* infrastructure.socialmedia.linkedin.JobDAO has a dependency with domain.linkedin.Company
//Result: TRUE
public class Company {

	private String name;
	private List<Person> employees;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Person> employees) {
		this.employees = employees;
	}
}