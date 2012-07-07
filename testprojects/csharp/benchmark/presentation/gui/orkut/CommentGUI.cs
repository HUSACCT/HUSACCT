using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.domain.orkut;

namespace CSharpBenchmark.presentation.gui.orkut
{
    //Functional requirement 3.1.2
    //Test case 69: Only class presentation.gui.orkut.CommentGUI may have a dependency with domain.orkut.Comment
    //Result: TRUE
    public class CommentGUI
    {
        //FR5.5
        private Comment comment;

        public CommentGUI()
        {
            //FR5.1
            Console.WriteLine(comment.getCommentType());
        }
    }
}