package husacct.analyse.domain.famix;

class FamixAccess extends FamixAssociation
{
	private String accesses;
	private String accessedIn;
	private boolean isAccessedLValue = false;
	
	public String getAccesses()
	{
		return accesses;
	}
	public void setAccesses(String accesses)
	{
		this.accesses = accesses;
	}

	public String getAccessedIn()
	{
		return accessedIn;
	}

	public void setAccessedIn(String accessedIn)
	{
		this.accessedIn = accessedIn;
	}

	public boolean isAccessedLValue()
	{
		return isAccessedLValue;
	}

	public void setAccessedLValue(boolean isAccessedLValue)
	{
		this.isAccessedLValue = isAccessedLValue;
	}
}
