using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.infrastructure.socialmedia.linkedin;

namespace CSharpBenchmark.domain.linkedin
{
    //Functional requirement 4.5
    //Test case 204: tThe following classes have a circular dependency:
    //					* domain.linkedin.Company has a dependency with domain.linkedin.Person
    //					* domain.linkedin.Person has a dependency with infrastructure.socialmedia.linkedin.JobDAO
    //					* infrastructure.socialmedia.linkedin.JobDAO has a dependency with domain.linkedin.Company
    //Result: TRUE
    public class Person
    {

        private String name;

        public List<String> getInterestedJobs()
        {
            return new JobDAO().getInterestedJobs();
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
}