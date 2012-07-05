using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.linkedin
{
    //Functional requirement 4.5
    //Test case 204: tThe following classes have a circular dependency:
    //					* domain.linkedin.Company has a dependency with domain.linkedin.Person
    //					* domain.linkedin.Person has a dependency with infrastructure.socialmedia.linkedin.JobDAO
    //					* infrastructure.socialmedia.linkedin.JobDAO has a dependency with domain.linkedin.Company
    //Result: TRUE
    public class Company
    {

        private string name;
        private List<Person> employees;

        public string getName()
        {
            return name;
        }

        public void setName(string name)
        {
            this.name = name;
        }

        public List<Person> getEmployees()
        {
            return employees;
        }

        public void setEmployees(List<Person> employees)
        {
            this.employees = employees;
        }
    }
}