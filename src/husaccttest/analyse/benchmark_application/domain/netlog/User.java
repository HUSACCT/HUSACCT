package husaccttest.analyse.benchmark_application.domain.netlog;
//Functional requirement 4.5
//Test case 203: the following classes have a circular dependency
//					* domain.netlog.Picture has a dependency with domain.netlog.Tag
//					* domain.netlog.Profile has a dependency with domain.netlog.Profile
//					* domain netlog.netlog.Profile has a dependency with domain.netlog.Picture
//					* domain.netlog.User has a dependency with domain.netlog.Picture
//Result: TRUE
public abstract class User {
	private Picture profilePicture;
		
	public Picture getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(Picture profilePicture) {
		this.profilePicture = profilePicture;
	}
	public abstract void abstractFunciton();
}