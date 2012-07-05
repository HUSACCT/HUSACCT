using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.socialmedia.pinterest
{

    public class PinDAO
    {

        public BoardDAO getBoardFromAPI()
        {
            return new BoardDAO();
        }

        public bool login(PinterestUserDTO dto)
        {
            return false;
        }

        public BoardDTO getBoardDetails(Object o)
        {
            return new BoardDTO();
        }
    }
}