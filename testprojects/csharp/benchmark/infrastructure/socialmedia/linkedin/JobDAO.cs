using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.linkedin;

namespace CSharpBenchmark.infrastructure.socialmedia.linkedin
{
    //Functional requirement 4.5
    //Test case 204: tThe following classes have a circular dependency:
    //					* domain.linkedin.Company has a dependency with domain.linkedin.Person
    //					* domain.linkedin.Person has a dependency with infrastructure.socialmedia.linkedin.JobDAO
    //					* infrastructure.socialmedia.linkedin.JobDAO has a dependency with domain.linkedin.Company
    //Result: TRUE
    public class JobDAO
    {

        public List<String> getInterestedJobs()
        {
            List<String> returnList = new List<String>();
            returnList.Add(new Company().getName());
            return returnList;
        }
    }
}