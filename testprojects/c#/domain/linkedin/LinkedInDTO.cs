using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.linkedin
{
    public class LinkedInDTO
    {
        private string name;
        private string password;

        public void setName(string name)
        {
            this.name = name;
        }

        public string getName()
        {
            return name;
        }

        public void setPassword(string password)
        {
            this.password = password;
        }

        public string getPassword()
        {
            return password;
        }
    }
}