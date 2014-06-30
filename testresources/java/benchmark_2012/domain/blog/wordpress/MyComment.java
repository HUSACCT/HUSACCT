package domain.blog.wordpress;

import infrastructure.blog.Blog;
import infrastructure.blog.ILocation;
//Functional requirement 3.3
//Test case 171: Class domain.blog.wordpress.MyComment is only allowed to have dependencies on infrastructure.blog.*. Exception: Not on infrastructure.blog.ILocation.

//Result: TRUE/FALSE

//FR5.4
public class MyComment extends Blog implements ILocation {

}