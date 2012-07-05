using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.lastfm
{

    public class Album
    {
        private string name;
        private DateTime releaseDate;

        public string getName()
        {
            return name;
        }

        public void setName(string name)
        {
            this.name = name;
        }

        public DateTime getReleaseDate()
        {
            return releaseDate;
        }

        public void setReleaseDate(DateTime releaseDate)
        {
            this.releaseDate = releaseDate;
        }
    }
}