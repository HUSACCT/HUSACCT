using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.presentation.annotations
{
    public class Copyright : Attribute
    {
        public string author
        {
            set;
            get;
        }

        public string version
        {
            set;
            get;
        }

        public string created
        {
            set;
            get;
        }

        public Copyright(string auth = "Themaopdracht 7 tester", string vers = "0.1", string creat = "2012")
        {
            author = auth;
            version = vers;
            created = creat;
        }
    }
}