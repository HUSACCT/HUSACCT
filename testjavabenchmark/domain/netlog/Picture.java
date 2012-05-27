package domain.netlog;

import java.util.ArrayList;
import java.util.List;
//Functional requirement 4.5
//Test case 203: the following classes have a circular dependency
//					* domain.netlog.Picture has a dependency with domain.netlog.Tag
//					* domain.netlog.Profile has a dependency with domain.netlog.Profile
//					* domain netlog.netlog.Profile has a dependency with domain.netlog.Picture
//					* domain.netlog.User has a dependency with domain.netlog.Picture
//Result: TRUE
public class Picture {
	List<Tag> tags = new ArrayList<Tag>();
}