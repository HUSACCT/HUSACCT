package domain.blog.wordpress;

import infrastructure.blog.Blog;
import infrastructure.blog.ILocation;

//Functional requirement 3.3
//Test case 170: Class domain.blog.wordpress.MyBlog is allowed to have dependencies on infrastructure.blog.*. Exception: Not on infrastructure.blog.ILocation.
//Result: TRUE/FALSE

//FR5.3
public class MyBlog extends Blog implements ILocation{

	}