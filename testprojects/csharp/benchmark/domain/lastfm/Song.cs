using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.lastfm
{
    public class Song
    {
        private string name;
        private DateTime release;
        private Album album;
        private Artist artist;

        public String getName()
        {
            return name;
        }

        public void setName(string name)
        {
            this.name = name;
        }

        public DateTime getRelease()
        {
            return release;
        }

        public void setRelease(DateTime release)
        {
            this.release = release;
        }

        public Album getAlbum()
        {
            return album;
        }

        public void setAlbum(Album album)
        {
            this.album = album;
        }

        public Artist getArtist()
        {
            return artist;
        }

        public void setArtist(Artist artist)
        {
            this.artist = artist;
        }
    }
}