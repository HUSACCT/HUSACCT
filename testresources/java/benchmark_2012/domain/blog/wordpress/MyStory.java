package domain.blog.wordpress;


import infrastructure.blog.Blog;
import infrastructure.blog.ILocation;

//Functional requirement 3.3
//Test case 172: Class domain.blog.wordpress.MyStory is the only class allowed to depend on infrastructure.blog.*. Exception: domain.blog.wordpress.MyBlog is also allowed.
//Result: TRUE/FALSE

//FR5.3
public class MyStory extends Blog implements ILocation{

	}