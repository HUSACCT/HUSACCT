package domain.music.rhapsody;

import infrastructure.blog.BlogAnnotion;
import infrastructure.blog.MyBlogStory;
//Functional requirement 3.3
//Test case 175: Class domain.blog.wordpress.MyBlog is allowed to have a dependency with the class in infrastructure.blog except when infrastructure.blog.BlogAnnotation is used
//Result: TRUE/FALSE

@BlogAnnotion
public class Payment {
	//FR5.2
	private MyBlogStory mystory;	
}